package com.shashankbhat.splitbill.util

data class Response<out T>(
    val status: Status = Status.Nothing,
    val data: T? = null,
    val message: String? = null
) {

    fun isSuccess(): Boolean {
        return status == Status.Success
    }

    fun isLoading(): Boolean {
        return status == Status.Loading
    }

    fun isError(): Boolean {
        return status == Status.Error
    }

    companion object {
        fun <T> success(data: T?): Response<T> {
            return Response(Status.Success, data, null)
        }

        fun <T> error(message: String?, data: T? = null): Response<T> {
            return Response(Status.Error, data, message)
        }

        fun <T> loading(data: T? = null): Response<T> {
            return Response(Status.Loading, data, null)
        }

        fun <T> nothing(): Response<T> {
            return Response(Status.Nothing, null, null)
        }

        fun <T> unauthorized(message: String?, data: T? = null): Response<T> {
            return Response(Status.Unauthorized, null, null)
        }
    }
}


enum class Status {
    Loading,
    Nothing,
    Success,
    Error,
    Unauthorized
}