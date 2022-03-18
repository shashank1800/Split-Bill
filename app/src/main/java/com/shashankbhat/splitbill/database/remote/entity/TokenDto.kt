package com.shashankbhat.splitbill.database.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    var token: String,
    var username: String,
    var uniqueId: Int
)