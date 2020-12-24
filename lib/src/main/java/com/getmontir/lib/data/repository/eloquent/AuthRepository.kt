package com.getmontir.lib.data.repository.eloquent

import android.content.Context
import com.getmontir.lib.data.network.APIService
import com.getmontir.lib.data.network.ApiResourceBound
import com.getmontir.lib.data.network.apiResource
import com.getmontir.lib.data.response.ApiResponse
import com.getmontir.lib.data.response.ResultWrapper
import com.getmontir.lib.presentation.session
import com.getmontir.lib.presentation.utils.SessionManager
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class AuthRepository(
    private val context: Context,
    private val api: APIService,
    private val sessionManager: SessionManager
) {

    @InternalCoroutinesApi
    fun customerLogin(
        email: String?,
        password: String?
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context, false) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.isLoggedIn = true
            sessionManager.token = items
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.token
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.customerLogin(email, password)
        }

    }.build()

    @InternalCoroutinesApi
    fun customerLoginSocial(
        token: String,
        fcmToken: String,
        channel: String,
        device: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context, false) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.isLoggedIn = true
            sessionManager.token = items
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.token
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.customerLoginSocial(token, fcmToken, channel, device)
        }

    }.build()

}