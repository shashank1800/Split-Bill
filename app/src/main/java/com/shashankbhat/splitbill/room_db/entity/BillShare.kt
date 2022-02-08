package com.shashankbhat.splitbill.room_db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "bill_share", foreignKeys = [
        ForeignKey(
            entity = Bill::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("bill_id"),
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_id"),
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class BillShare(
    @ColumnInfo(name = "bill_id")
    var billId: Int? = null,
    @ColumnInfo(name = "user_id")
    var userId: Int? = null,
    var spent: Float? = null,
    var share: Float? = null,
    @PrimaryKey
    var id: Int? = null,
    @ColumnInfo(name = "date_created")
    var dateCreated: Long? = null
)