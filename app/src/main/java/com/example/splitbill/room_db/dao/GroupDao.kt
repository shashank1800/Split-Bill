package com.example.splitbill.room_db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.splitbill.model.GroupListModel
import com.example.splitbill.room_db.entity.Groups

@Dao
interface GroupDao {

    @Insert
    suspend fun insert(group: Groups?)

    @Query("SELECT * FROM groups ORDER BY date_created")
    fun getAllGroups(): LiveData<List<GroupListModel>>?

}