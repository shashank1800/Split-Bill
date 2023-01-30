package com.shashankbhat.splitbill.database.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shashankbhat.splitbill.database.local.entity.Groups

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(group: Groups?)

    @Query("SELECT * FROM groups ORDER BY date_created DESC")
    suspend fun getAllGroups(): List<Groups>

    @Query("Update groups SET id = :remoteId WHERE id = :localId")
    suspend fun update(localId: Int, remoteId: Int)

    @Query("SELECT * FROM groups WHERE id < 1 ORDER BY date_created DESC")
    suspend fun getAllUnsavedGroups(): List<Groups>

}