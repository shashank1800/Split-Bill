package com.shashankbhat.splitbill.database.remote.entity

import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import kotlinx.serialization.Serializable

@Serializable
class GroupsAllDataDto {
    var data: List<GroupListDto>? = null
}
