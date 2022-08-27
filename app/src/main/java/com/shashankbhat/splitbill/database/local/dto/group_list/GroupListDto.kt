package com.shashankbhat.splitbill.database.local.dto.group_list

import com.shashankbhat.splitbill.database.local.dto.group_list.GroupsDto
import com.shashankbhat.splitbill.database.local.dto.users.UserDto
import java.io.Serializable

@kotlinx.serialization.Serializable
data class GroupListDto(
    val group: GroupsDto? = null,
    val userList: List<UserDto>? = null
) : Serializable