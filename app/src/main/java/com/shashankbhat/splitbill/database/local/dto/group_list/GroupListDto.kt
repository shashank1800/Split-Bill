package com.shashankbhat.splitbill.database.local.dto.group_list

import com.shashankbhat.splitbill.database.local.entity.Groups
import com.shashankbhat.splitbill.database.local.entity.User
import java.io.Serializable

@kotlinx.serialization.Serializable
data class GroupListDto(
    val group: Groups? = null,
    val userList: List<User>? = null
) : Serializable