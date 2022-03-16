package com.shashankbhat.splitbill.database.local.repository

import androidx.compose.runtime.MutableState
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.database.local.entity.Groups
import com.shashankbhat.splitbill.database.local.dao.GroupDao
import com.shashankbhat.splitbill.util.Response

class GroupRepository(private val groupDao: GroupDao) {

    suspend fun insert(group: Groups) {
        groupDao.insert(group)
    }

    suspend fun update(localId: Int, remoteId: Int) {
        groupDao.update(localId, remoteId)
    }

    suspend fun getAllGroups(groupsListState: MutableState<Response<List<GroupListDto>>>) {
        val groups = groupDao.getAllGroups()
        groupsListState.value = Response.success(groups)
    }

}