package com.getmontir.lib.data.repository.eloquent

import android.content.Context
import com.getmontir.lib.data.data.dto.StationRegisterContact
import com.getmontir.lib.data.data.dto.UserDto
import com.getmontir.lib.data.data.entity.UserEntity
import com.getmontir.lib.data.network.APIService
import com.getmontir.lib.data.network.ApiResourceBound
import com.getmontir.lib.data.response.ApiResponse
import com.getmontir.lib.data.response.ResultWrapper
import com.getmontir.lib.presentation.session
import com.getmontir.lib.presentation.utils.SessionManager
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.util.*

/**
 * Authentication Repository
 *
 * @param context
 * @param api
 * @param sessionManager
 */
class AuthRepository(
    private val context: Context,
    private val api: APIService,
    private val sessionManager: SessionManager
) {

    /**
     * Customer - Login via credential
     *
     * @param email
     * @param password
     * @return Flow<ResultWrapper<String>>
     */
    @InternalCoroutinesApi
    fun customerLogin(
        email: String?,
        password: String?
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
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
            return api.customerLoginAsync(email, password)
        }

    }.build()

    /**
     * Customer - Login via social media
     *
     * @param token
     * @param fcmToken
     * @param channel
     * @param device
     * @return Flow<ResultWrapper<String>>
     */
    @InternalCoroutinesApi
    fun customerLoginSocial(
        token: String,
        fcmToken: String,
        channel: String,
        device: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
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
            return api.customerLoginSocialAsync(token, fcmToken, channel, device)
        }

    }.build()

    /**
     * Customer - Register via credential
     *
     * @param name
     * @param phone
     * @param email
     * @param password
     * @param passwordConfirmation
     * @return Flow<ResultWrapper<String>>
     */
    @InternalCoroutinesApi
    fun customerRegister(
        name: String,
        phone: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {

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
            return api.customerRegisterAsync(name, phone, email, password, passwordConfirmation)
        }

    }.build()

    /**
     * Customer - Register via Social Media
     *
     * @param token
     * @param fcmToken
     * @param channel
     * @param device
     * @return Flow<ResultWrapper<String>>
     */
    @InternalCoroutinesApi
    fun customerRegisterSocial(
        token: String,
        fcmToken: String,
        channel: String,
        device: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {

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
            return api.customerRegisterSocialAsync(token, fcmToken, channel, device)
        }

    }.build()

    /**
     * Customer - Forgot Password
     *
     * @param email
     * @return Flow<ResultWrapper<String>>
     */
    @InternalCoroutinesApi
    fun customerForgotPassword(
        email: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.forgotEmail = email
            sessionManager.forgotToken = items
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.forgotToken
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.customerForgotPasswordAsync(email)
        }

    }.build()

    /**
     * Customer - Forgot Password Confirm
     *
     * @param otp
     * @param token
     * @param email
     * @return Flow<ResultWrapper<String>>
     */
    @InternalCoroutinesApi
    fun customerForgotPasswordConfirm(
        otp: String,
        token: String,
        email: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.forgotToken = items
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.forgotToken
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.customerForgotPasswordConfirmAsync( otp, token, email )
        }
    }.build()

    /**
     * Customer - Forgot Password Reset
     *
     * @param token
     * @param password
     * @param passwordConfirmation
     * @param email
     * @return Flow<ResultWrapper<String>>
     */
    @InternalCoroutinesApi
    fun customerForgotChangePassword(
        token: String,
        password: String,
        passwordConfirmation: String,
        email: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.forgotEmail = null
            sessionManager.forgotToken = null
        }

        override suspend fun loadFromDb(): String? {
            return null
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.customerForgotChangePasswordAsync(token, password, passwordConfirmation, email)
        }
    }.build()

    /**
     * Customer - Forgot Password Resend
     *
     * @param email
     * @return Flow<ResultWrapper<String>>
     */
    @InternalCoroutinesApi
    fun customerForgotPasswordResend(
        email: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.forgotToken = items
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.forgotToken
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.customerForgotPasswordResendAsync(email)
        }
    }.build()

    @InternalCoroutinesApi
    fun stationLogin(
        email: String,
        password: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.token = items
            sessionManager.isLoggedIn = true
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.token
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.stationLoginAsync(email, password)
        }
    }.build()

    /**
     *
     */
    @InternalCoroutinesApi
    fun stationLoginSocial(
        token: String,
        fcmToken: String,
        channel: String,
        device: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.token = items
            sessionManager.isLoggedIn = true
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.token
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.stationLoginSocialAsync(token, fcmToken, channel, device)
        }
    }.build()

    /**
     *
     */
    @InternalCoroutinesApi
    fun stationRegisterContact(): Flow<ResultWrapper<StationRegisterContact>> = object: ApiResourceBound<StationRegisterContact, ApiResponse<StationRegisterContact>>(context, false) {
        override fun processResponse(response: ApiResponse<StationRegisterContact>?): StationRegisterContact? {
            return response?.data
        }

        override fun shouldFetch(data: StationRegisterContact?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: StationRegisterContact) {
            // DO NOTHING
        }

        override suspend fun loadFromDb(): StationRegisterContact? {
            return null
        }

        override suspend fun createCallAsync(): Response<ApiResponse<StationRegisterContact>> {
            return api.stationRegisterContactAsync()
        }
    }.build()

    /**
     *
     */
    @InternalCoroutinesApi
    fun stationForgotPassword(
        email: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.forgotToken = items
            sessionManager.forgotEmail = email
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.forgotToken
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.stationForgotPasswordAsync(email)
        }
    }.build()

    /**
     *
     */
    @InternalCoroutinesApi
    fun stationForgotPasswordResend(
        email: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.forgotToken = items
            sessionManager.forgotEmail = email
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.forgotToken
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.stationForgotPasswordAsync(email)
        }
    }.build()

    /**
     * 
     */
    @InternalCoroutinesApi
    fun stationForgotPasswordConfirm(
        otp: String,
        token: String,
        email: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.forgotToken = items
            sessionManager.forgotEmail = email
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.forgotToken
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.stationForgotPasswordConfirmAsync(otp, token, email)
        }
    }.build()

    /**
     * Station - Forgot password resetting
     *
     * @param token
     * @param password
     * @param passwordConfirmation
     * @param email
     */
    @InternalCoroutinesApi
    fun stationForgotPasswordReset(
        token: String,
        password: String,
        passwordConfirmation: String,
        email: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.forgotToken = null
            sessionManager.forgotEmail = null
        }

        override suspend fun loadFromDb(): String? {
            return null
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.stationForgotChangePasswordAsync(token, password, passwordConfirmation, email)
        }
    }.build()

    /**
     * Get user profile data
     *
     * @return Flow<ResultWrapper<UserEntity>>
     */
    @InternalCoroutinesApi
    fun profile(): Flow<ResultWrapper<UserEntity>> = object: ApiResourceBound<UserEntity, ApiResponse<UserDto>>(context) {
        override fun processResponse(response: ApiResponse<UserDto>?): UserEntity? {
            return response?.data?.entity()
        }

        override fun shouldFetch(data: UserEntity?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: UserEntity) {
            sessionManager.userId = items.id
            sessionManager.userImage = items.image
            sessionManager.userName = items.name
            sessionManager.userEmail = items.email
            sessionManager.userBanned = items.banned
            sessionManager.userAccepted = items.accepted
            sessionManager.userInformationCompleted = items.informationCompleted
            sessionManager.userDocumentCompleted = items.documentCompleted
            sessionManager.userEmailVerifiedAt = items.emailVerified
        }

        override suspend fun loadFromDb(): UserEntity? {
            val userId = sessionManager.userId
            val userName = sessionManager.userName
            val userPhone = sessionManager.userPhone
            val userEmail = sessionManager.userEmail
            val userVerifiedAt = sessionManager.userEmailVerifiedAt
            if(
                userId != null
                && userName != null
                && userPhone != null
                && userEmail != null
                && userVerifiedAt != null
            ) {
                return UserEntity(
                    userId,
                    sessionManager.userImage,
                    userName,
                    userPhone,
                    userEmail,
                    sessionManager.userBanned,
                    sessionManager.userAccepted,
                    sessionManager.userInformationCompleted,
                    sessionManager.userDocumentCompleted,
                    userVerifiedAt,
                    lastRefreshed = Date()
                )
            } else {
                return null
            }
        }

        override suspend fun createCallAsync(): Response<ApiResponse<UserDto>> {
            return api.profileUserAsync()
        }

    }.build()

}