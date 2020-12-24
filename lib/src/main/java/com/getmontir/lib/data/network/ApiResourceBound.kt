package com.getmontir.lib.data.network

import android.content.Context
import com.getmontir.lib.data.exception.network.NoConnectivityException
import com.getmontir.lib.data.response.ResultWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection.*
import kotlin.reflect.KProperty

abstract class ApiResourceBound<ResultType : Any, RequestType : Any>(
    private val context: Context,
    private val withDatabase: Boolean = true
) {

    companion object {
        private val TAG: String = ApiResourceBound::class.java.name
    }

    protected abstract fun processResponse(response: RequestType?): ResultType?
    
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun saveCallResults(items: ResultType)

    protected abstract suspend fun loadFromDb(): ResultType?

    protected abstract suspend fun createCallAsync(): Response<RequestType>

    fun build(): Flow<ResultWrapper<ResultType>> {
        return flow<ResultWrapper<ResultType>> {
            Timber.tag(TAG).d("START")
            emit(ResultWrapper.Loading(true))
            var dbResult: ResultType? = null

            if( withDatabase ) {
                Timber.tag(TAG).d("Load from database")
                dbResult = loadFromDb()
            }

            try {
                var isFetch = false

                if( !withDatabase ) {
                    isFetch = true
                } else {
                    if( shouldFetch(dbResult) ) {
                        isFetch = true
                    }
                }

                Timber.tag(TAG).d("Fetching")
                if( isFetch ) {
                    Timber.tag(TAG).d("Fetch data from network")
                    val apiResponse = createCallAsync()

                    if( apiResponse.isSuccessful ) {
                        processResponse(apiResponse.body())?.let {
                            if( withDatabase ) {
                                saveCallResults(it)
                            }
                            emit(ResultWrapper.Success(it))
                        }
                    }
                } else {
                    emit(ResultWrapper.Success(dbResult))
                }
            } catch( t: Exception ) {
                Timber.tag(TAG).e(t)
                when( t ) {
                    is IOException -> emit(ResultWrapper.Error.Network.NoConnectivity(t))
                    is HttpException -> {
                        when(t.code()) {
                            HTTP_BAD_REQUEST -> emit(ResultWrapper.Error.Http.BadRequest(t))
                            HTTP_NOT_FOUND -> emit(ResultWrapper.Error.Http.NotFound(t))
                            HTTP_UNAUTHORIZED -> emit(ResultWrapper.Error.Http.Unauthorized(t))
                            HTTP_BAD_METHOD -> emit(ResultWrapper.Error.Http.Validation(t))
                            HTTP_UNAVAILABLE -> emit(ResultWrapper.Error.Http.Maintenance(t))
                            else -> emit(ResultWrapper.Error.GenericError(t))
                        }
                    }
                    else -> emit(ResultWrapper.Error.GenericError(t))
                }
            } catch ( t: NoConnectivityException ) {
                Timber.tag(TAG).e(t)
                emit(ResultWrapper.Error.Network.NoConnectivity(t))
            } finally {
                Timber.tag(TAG).d("FINISH")
                emit(ResultWrapper.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

}

fun <S: Any, R: Any> apiResource(
    context: Context,
    callResponse: (response: R?) -> S?,
    shouldFetch: Boolean,
    saveResult: suspend (items: S) -> Unit,
    load: suspend CoroutineScope.() -> S,
    callAsync: suspend CoroutineScope.() -> Response<R>
) = apiResource(
    context,
    true,
    callResponse,
    shouldFetch,
    saveResult,
    load,
    callAsync
)
fun <S: Any, R: Any> apiResource(
    context: Context,
    withDatabase: Boolean,
    callResponse: (response: R?) -> S?,
    shouldFetch: Boolean,
    saveResult: suspend (items: S) -> Unit,
    load: suspend CoroutineScope.() -> S,
    callAsync: suspend CoroutineScope.() -> Response<R>
) = ApiResourceDelegate<S,R>(
    context,
    withDatabase,
    callResponse,
    shouldFetch,
    saveResult,
    load,
    callAsync
)
class ApiResourceDelegate<S : Any,R: Any>(
    context: Context,
    withDatabase: Boolean = true,
    private val callResponse: (response: R?) -> S?,
    private val shouldFetch: Boolean,
    private val saveResult: suspend (items: S) -> Unit,
    private val load: suspend CoroutineScope.() -> S,
    private val callAsync: suspend CoroutineScope.() -> Response<R>
): ApiResourceBound<S,R>(context, withDatabase) {

    override fun processResponse(response: R?): S? {
        return callResponse(response)
    }

    override fun shouldFetch(data: S?): Boolean {
        return shouldFetch
    }

    override suspend fun saveCallResults(items: S) {
        saveResult(items)
    }

    override suspend fun loadFromDb(): S? {
        var item: S?
        coroutineScope {
            item = load()
        }
        return item
    }

    override suspend fun createCallAsync(): Response<R> {
        var item: Response<R>
         coroutineScope {
            item = callAsync()
        }
        return item
    }
    
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = build()

}