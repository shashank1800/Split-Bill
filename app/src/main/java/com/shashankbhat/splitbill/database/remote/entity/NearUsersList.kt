package com.shashankbhat.splitbill.database.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class NearUsersList(var users : ArrayList<NearUserDto>)