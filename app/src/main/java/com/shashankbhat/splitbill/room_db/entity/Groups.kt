package com.shashankbhat.splitbill.room_db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class Groups(
    var name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(name = "date_created")
    var dateCreated: Long = System.currentTimeMillis()
}