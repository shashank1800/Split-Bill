package com.shashankbhat.splitbill.room_db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "bill", foreignKeys = [ForeignKey(
        entity = Groups::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("group_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Bill(
    @ColumnInfo(name = "group_id")
    var groupId: Int,
    var name: String,
    @ColumnInfo(name = "total_amount")
    var totalAmount: Float
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(name = "date_created")
    var dateCreated: Long = System.currentTimeMillis()
}