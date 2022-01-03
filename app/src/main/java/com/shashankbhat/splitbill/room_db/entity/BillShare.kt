package com.shashankbhat.splitbill.room_db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "bill_share", foreignKeys = [
        ForeignKey(
            entity = Bill::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("bill_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BillShare(
    var bill_id: Int,
    var user_id: Int,
    var spent: Float,
    var share: Float
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var date_created: Long = System.currentTimeMillis()
}