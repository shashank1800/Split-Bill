package com.shashankbhat.splitbill.repository.local

import androidx.lifecycle.LiveData
import com.shashankbhat.splitbill.model.GroupListModel
import com.shashankbhat.splitbill.room_db.entity.Groups
import com.shashankbhat.splitbill.room_db.dao.GroupDao

class GroupRepository(private val groupDao: GroupDao) {

    suspend fun insert(group: Groups) {
        groupDao.insert(group)
    }

    fun getAllGroups(): LiveData<List<GroupListModel>>? {
        return groupDao.getAllGroups()
    }

}