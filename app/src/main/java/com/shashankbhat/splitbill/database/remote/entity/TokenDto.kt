package com.shashankbhat.splitbill.database.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    var token: String? = null,
    var username: String? = null,
    var uniqueId: Int? = null
)