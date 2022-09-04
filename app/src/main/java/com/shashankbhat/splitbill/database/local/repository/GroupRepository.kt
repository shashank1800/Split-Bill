package com.shashankbhat.splitbill.database.local.repository

import androidx.lifecycle.MutableLiveData
import com.shashankbhat.splitbill.database.local.entity.Groups
import com.shashankbhat.splitbill.database.local.dao.GroupDao
import com.shashankbhat.splitbill.database.local.dao.UserDao
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupRecyclerListDto
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupsDto
import com.shashankbhat.splitbill.database.local.dto.users.UserDto
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
        groups.forEachIndexed { index, it ->
            val users = userDao.getAllUserByGroupId(it.id ?: 0)?.map { user ->
                UserDto(
                    user.name,
                    user.groupId,
                    user.id,
                    user.photoUrl,
                    user.dateCreated,
                    user.uniqueId
                )
            }?.toList()

            val adapter = if((groupsListState.value?.data?.size ?: 0) >= groups.size) groupsListState.value?.data?.get(index)?.adapter else null

            groupRecyclerArray.add(
                GroupRecyclerListDto(
                    GroupsDto(
                        it.name,
                        it.id,
                        it.dateCreated,
                        it.uniqueId
                    ),
                    users,
                    adapter
                )
            )
        }
        groupsListState.value = Response.success(groupRecyclerArray)
    }

    suspend fun getAllUnsavedGroups(): List<Groups> {
        return groupDao.getAllUnsavedGroups()
    }

}