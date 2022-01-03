package com.shashankbhat.splitbill.room_db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class Groups(
    var name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var date_created: Long = System.currentTimeMillis()
}