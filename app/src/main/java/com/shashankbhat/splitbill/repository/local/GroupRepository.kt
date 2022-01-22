package com.shashankbhat.splitbill.repository.local

import androidx.compose.runtime.MutableState
import com.shashankbhat.splitbill.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.room_db.entity.Groups
import com.shashankbhat.splitbill.room_db.dao.GroupDao
import com.shashankbhat.splitbill.util.Response

class GroupRepository(private val groupDao: GroupDao) {

    suspend fun insert(group: Groups) {
        groupDao.insert(group)
    }

    suspend fun getAllGroups(groupsListState: MutableState<Response<List<GroupListDto>>>) {
        val groups = groupDao.getAllGroups()
        groupsListState.value = Response.success(groups)
    }

}