package com.shashankbhat.splitbill.room_db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "user", foreignKeys = [ForeignKey(
        entity = Groups::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("group_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class User(
    var name: String,
    @ColumnInfo(name = "group_id")
    var groupId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "date_created")
    var dateCreated: Long = System.currentTimeMillis()
}