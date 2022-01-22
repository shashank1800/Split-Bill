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

        fun <T> error(message: String?): Response<T> {
            return Response(Status.Error, null, message)
        }

        fun <T> loading(): Response<T> {
            return Response(Status.Loading, null, null)
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