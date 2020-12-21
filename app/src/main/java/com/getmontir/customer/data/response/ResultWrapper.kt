package com.getmontir.customer.data.response

sealed class ResultWrapper<out T: Any> {
    data class Success<out T: Any>(val data: Any?): ResultWrapper<T>()
    data class Error(val exception: Exception): ResultWrapper<Nothing>()
    data class Loading(val loading: Boolean): ResultWrapper<Nothing>()
    val <T> T.exhaustive: T
        get() = this
}
