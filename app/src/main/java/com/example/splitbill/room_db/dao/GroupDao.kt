package com.example.splitbill.room_db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.splitbill.model.GroupListModel
import com.example.splitbill.room_db.entity.Group

@Dao
interface GroupDao {

    @Insert
    suspend fun insert(group: Group?)

    @Query("SELECT * FROM group_tbl ORDER BY date_created")
    fun getAllGroups(): LiveData<List<GroupListModel>>?

}