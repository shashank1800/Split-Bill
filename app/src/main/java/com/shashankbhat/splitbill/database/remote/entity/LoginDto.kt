package com.shashankbhat.splitbill.database.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    var username: String?,
    var password: String?
)