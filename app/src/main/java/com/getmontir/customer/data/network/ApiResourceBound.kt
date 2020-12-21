package com.getmontir.customer.data.network

import android.content.Context
import com.getmontir.customer.data.response.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

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
                emit(ResultWrapper.Error(t))
            } finally {
                emit(ResultWrapper.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

}