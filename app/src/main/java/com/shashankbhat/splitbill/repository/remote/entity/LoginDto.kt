package com.shashankbhat.splitbill.repository.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    var username: String,
    var password: String
)