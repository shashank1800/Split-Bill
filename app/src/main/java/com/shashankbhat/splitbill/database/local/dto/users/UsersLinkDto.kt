package com.shashankbhat.splitbill.database.local.dto.users

import kotlinx.serialization.Serializable

@Serializable
data class UsersLinkDto(
    var id: Int? = null,
    var uniqueId: Int? = null
)