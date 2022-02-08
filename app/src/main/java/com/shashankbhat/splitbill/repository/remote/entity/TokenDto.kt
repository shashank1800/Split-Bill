package com.shashankbhat.splitbill.repository.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    var token: String
)