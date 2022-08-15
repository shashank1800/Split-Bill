package com.shashankbhat.splitbill.util

import io.ktor.client.statement.*

data class KnownException(var httpResponse: HttpResponse, var errorMessage: String) : Exception(errorMessage)
