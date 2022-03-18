package com.shashankbhat.splitbill.database.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "user", foreignKeys = [ForeignKey(
        entity = Groups::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("group_id"),
        onDelete = ForeignKey.NO_ACTION
    )]
)
data class User(
    var name: String,

    @ColumnInfo(name = "group_id")
    var groupId: Int,

    @PrimaryKey(autoGenerate = false)
    var id: Int? = null,

    @ColumnInfo(name = "date_created")
    var dateCreated: Long? = System.currentTimeMillis(),

    @ColumnInfo(name = "unique_id")
    var uniqueId: Int? = null
)