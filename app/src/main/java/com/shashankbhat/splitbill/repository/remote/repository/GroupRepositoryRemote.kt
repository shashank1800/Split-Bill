package com.shashankbhat.splitbill.repository.remote.repository

import androidx.compose.runtime.MutableState
import com.shashankbhat.splitbill.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.repository.local.GroupRepository
import com.shashankbhat.splitbill.repository.remote.entity.GroupsAllDataDto
import com.shashankbhat.splitbill.room_db.entity.Groups
import com.shashankbhat.splitbill.ui.ApiConstants.BASE_URL
import com.shashankbhat.splitbill.ui.ApiConstants.allGroups
import com.shashankbhat.splitbill.ui.ApiConstants.saveGroup
import com.shashankbhat.splitbill.util.Response
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class GroupRepositoryRemote @Inject constructor(
    private val httpClient: HttpClient,
    private val groupRepository: GroupRepository
) {
    suspend fun insert(group: Groups) {
        try {
            val response = httpClient.post<Int>(BASE_URL + saveGroup) {
                contentType(ContentType.Application.Json)
                body = group
            }

            group.id = response
            groupRepository.insert(group)
        }catch (ex:Exception){

        }
    }

    suspend fun getAllGroups(groupsListState: MutableState<Response<List<GroupListDto>>>) {
        try {
            groupRepository.getAllGroups(groupsListState)
            val response = httpClient.get<GroupsAllDataDto>(BASE_URL + allGroups)
            groupsListState.value = Response.success(response.data)

            response.data?.forEach { it ->
                groupRepository.insert(it.group)
            }
        }catch (ex: Exception){
            groupsListState.value = Response.error("Something went wrong")
        }
    }
}
