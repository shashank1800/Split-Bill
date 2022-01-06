package com.shashankbhat.splitbill.dto.group_list

import androidx.room.Embedded
import androidx.room.Relation
import com.shashankbhat.splitbill.room_db.entity.Groups
import com.shashankbhat.splitbill.room_db.entity.User
import java.io.Serializable

data class GroupListDto(
    @Embedded
    val group: Groups,
    @Relation(parentColumn = "id",
        entityColumn = "group_id")
    val userList: List<User>
): Serializable