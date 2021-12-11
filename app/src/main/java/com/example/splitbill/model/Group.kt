package com.example.splitbill.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group_tbl")
data class Group(
    var name: String,
    var date_created: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}