package com.shashankbhat.splitbill.database.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class SaveProfileDto(
    var name: String? = null,
    var photoUrl: String? = null,
    var isNearbyVisible: Boolean? = null,
    var distanceRange: Double? = null
)