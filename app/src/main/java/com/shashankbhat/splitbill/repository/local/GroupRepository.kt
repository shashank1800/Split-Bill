package com.shashankbhat.splitbill.repository.local

import androidx.lifecycle.LiveData
import com.shashankbhat.splitbill.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.room_db.entity.Groups
import com.shashankbhat.splitbill.room_db.dao.GroupDao

class GroupRepository(private val groupDao: GroupDao) {

    suspend fun insert(group: Groups) {
        groupDao.insert(group)
    }

    fun getAllGroups(): LiveData<List<GroupListDto>>? {
        return groupDao.getAllGroups()
    }

}