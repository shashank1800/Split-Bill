package com.example.splitbill.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.splitbill.room_db.entity.Bill
import com.example.splitbill.room_db.entity.BillShare
import com.example.splitbill.room_db.entity.User
import java.io.Serializable

data class BillListModel(
    @Embedded
    val bill: Bill?,

    @Relation(parentColumn = "id",
        entityColumn = "bill_id")
    val userList: List<BillShare>?
): Serializable