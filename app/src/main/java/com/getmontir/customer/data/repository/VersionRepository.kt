package com.getmontir.customer.data.repository

import android.content.Context
import com.getmontir.customer.data.network.APIService
import com.getmontir.customer.data.network.ApiResourceBound
import com.getmontir.customer.data.response.ApiResponse
import com.getmontir.customer.data.response.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class VersionRepository(
    private val context: Context,
    private val api: APIService
) {
    @InternalCoroutinesApi
    fun getVersionCustomer(version: Int): Flow<ResultWrapper<Boolean>> {
//        return flow {
//            val list = api.checkUpdateCustomerAsync(version)
//            emit(list)
//        }.flowOn(Dispatchers.IO)
        return object: ApiResourceBound<Boolean, ApiResponse<Boolean>>(context, false) {
            override fun processResponse(response: ApiResponse<Boolean>?): Boolean? {
                return response?.data
            }

            override fun shouldFetch(data: Boolean?): Boolean {
                return true
            }

            override suspend fun saveCallResults(items: Boolean) {
                // DO NOTHING
            }

            override suspend fun loadFromDb(): Boolean? {
                return null
            }

            override suspend fun createCallAsync(): Response<ApiResponse<Boolean>> {
                return api.checkUpdateCustomerAsync(version)
            }

        }.build()
    }
}