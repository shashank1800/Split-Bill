package com.shashankbhat.splitbill.database.remote.repository

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.database.local.repository.GroupRepository
import com.shashankbhat.splitbill.database.remote.entity.GroupsAllDataDto
import com.shashankbhat.splitbill.database.local.entity.Groups
import com.shashankbhat.splitbill.ui.ApiConstants.AUTHORIZATION
import com.shashankbhat.splitbill.BuildConfig.BASE_URL
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupRecyclerListDto
import com.shashankbhat.splitbill.database.local.entity.User
import com.shashankbhat.splitbill.database.local.repository.UserRepository
import com.shashankbhat.splitbill.database.remote.entity.GroupSaveDto
import com.shashankbhat.splitbill.ui.ApiConstants.allGroups
import com.shashankbhat.splitbill.ui.ApiConstants.saveGroup
import com.shashankbhat.splitbill.util.DatabaseOperation
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.extension.getLocalId
import com.shashankbhat.splitbill.util.extension.getToken
import com.shashankbhat.splitbill.util.extension.getUniqueId
import com.shashankbhat.splitbill.util.extension.releaseOne
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class GroupRepositoryRemote @Inject constructor(
    private val httpClient: HttpClient,
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun insert(group: Groups, addLocalCallback: (type: DatabaseOperation) -> Unit) {
        try {
            group.id = sharedPreferences.getLocalId()
            group.uniqueId = sharedPreferences.getUniqueId()
            groupRepository.insert(group)
            addLocalCallback(DatabaseOperation.LOCAL)

            val remoteGroup = httpClient.post<GroupListDto>(BASE_URL + saveGroup) {
                contentType(ContentType.Application.Json)
                header(AUTHORIZATION, sharedPreferences.getToken())
                body = group
            }

            val localId = group.id ?: 0
            remoteGroup.group?.id?.let { groupRepository.update(localId, it) }
            addLocalCallback(DatabaseOperation.REMOTE)
            sharedPreferences.releaseOne()
        } catch (ex: Exception) {
        }
    }

    suspend fun getAllGroups(groupsListState: MutableLiveData<Response<List<GroupRecyclerListDto>>>) {

        try {
            groupRepository.getAllGroups(groupsListState)
            groupsListState.value = Response.loading(groupsListState.value?.data)
            val token = sharedPreferences.getToken()
            val response = httpClient.get<GroupsAllDataDto>(BASE_URL + allGroups) {
                header(AUTHORIZATION, token)
            }
            response.data?.forEach { it ->
                it.group?.let { group ->
                    groupRepository.insert(Groups(group.name, group.id, group.dateCreated ?: System.currentTimeMillis(), group.uniqueId))
                }

                it.userList?.forEach { user ->
                    userRepository.insert(User(user.name, user.groupId, user.id, user.photoUrl, user.dateCreated, user.uniqueId))
                }
            }

            val groupRecyclerArray = ArrayList<GroupRecyclerListDto>()
            response.data?.forEach {
                groupRecyclerArray.add(GroupRecyclerListDto(it.group, it.userList, null))
            }
            groupsListState.value = Response.success(groupRecyclerArray)
        } catch (ce: ClientRequestException) {
            if (ce.response.status == HttpStatusCode.Forbidden)
                groupsListState.value =
                    Response.unauthorized("Something went wrong", groupsListState.value?.data)
        } catch (ex: Exception) {
            groupsListState.value =
                Response.error("Something went wrong $ex", groupsListState.value?.data)
        }
    }

    suspend fun insertWithPeople(groupName: String, peoples: List<Int>?): Int? {
        return try {

            val remoteId = httpClient.post<GroupListDto>(BASE_URL + saveGroup) {
                contentType(ContentType.Application.Json)
                header(AUTHORIZATION, sharedPreferences.getToken())
                body = GroupSaveDto(groupName, peoples)
            }

            remoteId.group?.id
        } catch (ex: Exception) {
            null
        }
    }
}
