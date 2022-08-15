package com.shashankbhat.splitbill.util

import kotlinx.serialization.Serializable

@Serializable
data class KnownExceptionDto(
    var error: String? = null,
    var developerMessage: String? = null,
    var status: String? = null,
    var code: Int? = null,
    var timestamp: String? = null
)
