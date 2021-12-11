package com.example.splitbill.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GroupDao {

    @Insert
    suspend fun insert(group: Group?)

    @Query("SELECT * FROM group_tbl ORDER BY date_created")
    fun getAllGroups(): LiveData<List<Group>>?

}