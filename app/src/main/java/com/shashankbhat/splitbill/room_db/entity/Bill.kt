package com.shashankbhat.splitbill.room_db.entity

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
    var group_id: Int,
    var name: String,
    var total_amount: Float
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var date_created: Long = System.currentTimeMillis()
}