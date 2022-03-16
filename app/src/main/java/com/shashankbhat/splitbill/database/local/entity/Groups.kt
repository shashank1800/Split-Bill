package com.shashankbhat.splitbill.database.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "groups")
data class Groups(
    var name: String,
    @PrimaryKey(autoGenerate = false)
    var id: Int? = null,
    @ColumnInfo(name = "date_created")
    var dateCreated: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "unique_id")
    var uniqueId: Int? = null,
    @ColumnInfo(name = "is_uploaded")
    var isUploaded: Boolean? = null
)