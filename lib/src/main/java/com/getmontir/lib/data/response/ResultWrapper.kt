package com.getmontir.lib.data.response

sealed class ResultWrapper<out T: Any> {
    data class Success<out T: Any>(val data: Any?): ResultWrapper<T>()
    sealed class Error(val exception: Exception): ResultWrapper<Nothing>() {
        class GenericError(exception: Exception): Error(exception)
        sealed class Http(val e: Exception): Error(e) {
            class BadRequest(exception: Exception): Http(exception)
            class NotFound(exception: Exception): Http(exception)
            class Maintenance(exception: Exception): Http(exception)
            class Unauthorized(exception: Exception): Http(exception)
            class Validation(exception: Exception): Http(exception)
        }
        sealed class Network(val e: Exception): Error(e) {
            class NoConnectivity(exception: Exception): Network(exception)
        }
    }
    data class Loading(val loading: Boolean): ResultWrapper<Nothing>()
    val <T> T.exhaustive: T
        get() = this
}
