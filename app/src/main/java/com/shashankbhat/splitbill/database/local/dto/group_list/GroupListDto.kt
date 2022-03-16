package com.shashankbhat.splitbill.database.local.dto.group_list

import androidx.room.Embedded
import androidx.room.Relation
import com.shashankbhat.splitbill.database.local.entity.Groups
import com.shashankbhat.splitbill.database.local.entity.User
import java.io.Serializable

@kotlinx.serialization.Serializable
data class GroupListDto(
    @Embedded
    val group: Groups,
    @Relation(
        parentColumn = "id",
        entityColumn = "group_id"
    )
    val userList: List<User>
) : Serializable