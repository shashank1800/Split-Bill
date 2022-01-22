package com.shashankbhat.splitbill.repository.remote.entity

import com.shashankbhat.splitbill.dto.group_list.GroupListDto
import kotlinx.serialization.Serializable

@Serializable
class GroupsAllDataDto {
    var data: List<GroupListDto>? = null
}
