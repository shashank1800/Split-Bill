package com.shashankbhat.splitbill.database.remote.entity

import com.shashankbhat.splitbill.database.local.entity.User
import kotlinx.serialization.Serializable


@Serializable
class UsersAllDataDto {
    var data: List<User>? = null
}