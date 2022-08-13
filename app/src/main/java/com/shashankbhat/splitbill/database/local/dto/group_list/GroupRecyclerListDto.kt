package com.shashankbhat.splitbill.database.local.dto.group_list

import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shashankbhat.splitbill.database.local.entity.Groups
import com.shashankbhat.splitbill.database.local.entity.User
import com.shashankbhat.splitbill.databinding.AdapterGroupUsersProfileBinding

data class GroupRecyclerListDto(
    val group: Groups?,
    val userList: List<User>?,
    var adapter: RecyclerGenericAdapter<AdapterGroupUsersProfileBinding, User>? = null
)
