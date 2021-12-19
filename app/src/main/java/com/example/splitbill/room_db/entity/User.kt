package com.example.splitbill.room_db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "users_tbl", foreignKeys = [ForeignKey(
        entity = Group::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("group_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class User(
    var name: String,
    var group_id: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var date_created: Long = System.currentTimeMillis()
}