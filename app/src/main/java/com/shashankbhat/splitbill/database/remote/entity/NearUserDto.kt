package com.shashankbhat.splitbill.database.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class NearUserDto(
    val uniqueId: Int?,
    val name: String?,
    val photoUrl: String?,
    val latitude: Double?,
    val longitude: Double?
)