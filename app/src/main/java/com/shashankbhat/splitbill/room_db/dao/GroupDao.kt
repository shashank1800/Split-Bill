package com.shashankbhat.splitbill.room_db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.shashankbhat.splitbill.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.room_db.entity.Groups

@Dao
interface GroupDao {

    @Insert
    suspend fun insert(group: Groups?)

    @Query("SELECT * FROM groups ORDER BY date_created")
    fun getAllGroups(): LiveData<List<GroupListDto>>?

}