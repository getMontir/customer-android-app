package com.getmontir.lib.data.network

import android.content.Context
import com.getmontir.lib.data.exception.network.NoConnectivityException
import com.getmontir.lib.data.response.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection.*

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
            emit(ResultWrapper.Loading(true))
            var dbResult: ResultType? = null

            if( withDatabase ) {
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
                emit(ResultWrapper.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

}