package com.shashankbhat.splitbill.database.local.dto.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileNameDto(
    var name: String? = null
)