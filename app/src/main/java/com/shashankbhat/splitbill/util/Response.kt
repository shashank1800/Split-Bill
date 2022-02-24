package com.shashankbhat.splitbill.util

data class Response<out T>(
    val status: Status = Status.Nothing,
    val data: T? = null,
    val message: String? = null
) {
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

        fun <T> isNothing(): Response<T> {
            return Response(Status.Nothing, null, null)
        }
    }
}


enum class Status {
    Loading,
    Nothing,
    Success,
    Error
}