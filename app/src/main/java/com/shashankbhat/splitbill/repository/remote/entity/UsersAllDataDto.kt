package com.shashankbhat.splitbill.repository.remote.entity

import com.shashankbhat.splitbill.room_db.entity.User
import kotlinx.serialization.Serializable


@Serializable
class UsersAllDataDto {
    var data: List<User>? = null
}