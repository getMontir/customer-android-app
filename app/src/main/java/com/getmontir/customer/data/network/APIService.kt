package com.getmontir.customer.data.network

import com.getmontir.customer.data.response.ApiResponse
import com.getmontir.customer.BuildConfig
import com.getmontir.customer.data.ext.bodyToString
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

interface APIService {
    @FormUrlEncoded
    @POST("check-update/customer")
    suspend fun checkUpdateCustomerAsync(
        @Field("req_version") version: Int
    ): Response<ApiResponse<Boolean>>

    @FormUrlEncoded
    @POST("check-update/station")
    suspend fun checkUpdateStationAsync(
        @Field("req_version") version: Int
    ): Response<ApiResponse<Boolean>>

    companion object {

        private const val debugURL = "http://getmontir.paperplane.id/api/"
        private const val releaseURL = "https://api.getmontir.com/api/"

        fun createService( /*sessionManager: SessionManager*/): APIService {
            val baseURL = if( BuildConfig.DEBUG ) debugURL else releaseURL
            val appToken = "bCtgjoy3gGQHAdoyzFbduGhAGr5hQND5Fbt7ggMWNgi10_dcPBmr9cHc5tK9v"
            val cacheTime = 432000

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor { chain ->
                    val newRequest = chain.request()
                    val request: Request

                    val builder = newRequest.newBuilder()

//                    if( sessionManager.isLoggedIn ) {
//                        builder.header("Authorization", "Bearer " + sessionManager.token)
//                    }

                    if( listOf("put", "patch", "delete").contains(
                            newRequest.method.toLowerCase(
                                Locale.ROOT
                            )
                        )
                    ) {
                        val body = newRequest.body
                        val method = newRequest.method
                        builder.post(
                            (body.bodyToString() + "&method=$method"
                                    ).toRequestBody(body?.contentType())
                        )
                    }

                    builder
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Accept", "application/json")
                        .removeHeader("Pragma")
                        .addHeader("Cache-Control", String.format("max-age=%d", cacheTime))
                        .addHeader("X-G-Access-Token", appToken)

                    request = builder.build()

                    Timber.d("API URL:: ${request.url}")

                    chain.proceed(request)
                }
                .build()

            return Retrofit.Builder()
                .baseUrl(baseURL)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(APIService::class.java)
        }
    }
}