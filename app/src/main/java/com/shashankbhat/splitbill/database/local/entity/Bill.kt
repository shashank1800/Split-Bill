package com.shashankbhat.splitbill.database.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "bill", foreignKeys = [ForeignKey(
        entity = Groups::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("group_id"),
        onDelete = ForeignKey.NO_ACTION
    )]
)
data class Bill(
    @ColumnInfo(name = "group_id")
    var groupId: Int,
    var name: String,
    @ColumnInfo(name = "total_amount")
    var totalAmount: Float,
    @PrimaryKey
    var id: Int? = null,
    @ColumnInfo(name = "date_created")
    var dateCreated: Long? = null
)