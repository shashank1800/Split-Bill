package com.shashankbhat.splitbill.database.local.repository

import androidx.lifecycle.MutableLiveData
import com.shashankbhat.splitbill.database.local.entity.Groups
import com.shashankbhat.splitbill.database.local.dao.GroupDao
import com.shashankbhat.splitbill.database.local.dao.UserDao
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupRecyclerListDto
import com.shashankbhat.splitbill.util.Response

class GroupRepository(private val groupDao: GroupDao, private val userDao: UserDao) {

    suspend fun insert(group: Groups) {
        groupDao.insert(group)
    }

    suspend fun update(localId: Int, remoteId: Int) {
        groupDao.update(localId, remoteId)
    }

    suspend fun getAllGroups(groupsListState: MutableLiveData<Response<List<GroupRecyclerListDto>>>) {
        val groups = groupDao.getAllGroups()

        val groupRecyclerArray = ArrayList<GroupRecyclerListDto>()
        groups.forEach {
            groupRecyclerArray.add(GroupRecyclerListDto(it, userDao.getAllUserByGroupId(it.id ?:  0), null))
        }
        groupsListState.value = Response.success(groupRecyclerArray)
    }

}