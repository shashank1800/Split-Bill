package com.shashankbhat.splitbill.database.remote.entity

import kotlinx.serialization.Serializable

@Serializable
class LocationDto(
    var latitude: Double? = null,
    var longitude: Double? = null
)