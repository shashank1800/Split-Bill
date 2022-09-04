package com.shashankbhat.splitbill.database.local.dto.users

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    var name: String,
    var groupId: Int,
    var id: Int? = null,
    var photoUrl: String? = null,
    var dateCreated: Long? = System.currentTimeMillis(),
    var uniqueId: Int? = null
)