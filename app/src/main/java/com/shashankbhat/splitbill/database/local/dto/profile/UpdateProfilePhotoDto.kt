package com.shashankbhat.splitbill.database.local.dto.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfilePhotoDto(
    var photoUrl: String? = null
)