package com.example.splitbill.repository.local

import androidx.lifecycle.LiveData
import com.example.splitbill.model.GroupListModel
import com.example.splitbill.room_db.entity.Groups
import com.example.splitbill.room_db.dao.GroupDao

class GroupRepository(private val groupDao: GroupDao) {

    suspend fun insert(group: Groups) {
        groupDao.insert(group)
    }

    fun getAllGroups(): LiveData<List<GroupListModel>>? {
        return groupDao.getAllGroups()
    }

}