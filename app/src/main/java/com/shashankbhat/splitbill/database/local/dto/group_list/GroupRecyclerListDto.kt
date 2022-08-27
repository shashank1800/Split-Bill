package com.shashankbhat.splitbill.database.local.dto.group_list

import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shashankbhat.splitbill.database.local.dto.users.UserDto
import com.shashankbhat.splitbill.databinding.AdapterGroupUsersProfileBinding

data class GroupRecyclerListDto(
    val group: GroupsDto?,
    val userList: List<UserDto>?,
    var adapter: RecyclerGenericAdapter<AdapterGroupUsersProfileBinding, UserDto>? = null
)
