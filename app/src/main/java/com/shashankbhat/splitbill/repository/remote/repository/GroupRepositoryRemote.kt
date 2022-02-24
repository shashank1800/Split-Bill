package com.shashankbhat.splitbill.repository.remote.repository

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import com.shashankbhat.splitbill.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.repository.local.GroupRepository
import com.shashankbhat.splitbill.repository.remote.entity.GroupsAllDataDto
import com.shashankbhat.splitbill.room_db.entity.Groups
import com.shashankbhat.splitbill.ui.ApiConstants
import com.shashankbhat.splitbill.ui.ApiConstants.AUTHORIZATION
import com.shashankbhat.splitbill.ui.ApiConstants.BASE_URL
import com.shashankbhat.splitbill.ui.ApiConstants.allGroups
import com.shashankbhat.splitbill.ui.ApiConstants.saveGroup
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.extension.getToken
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class GroupRepositoryRemote @Inject constructor(
    private val httpClient: HttpClient,
    private val groupRepository: GroupRepository,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun insert(group: Groups) {
        try {
            val response = httpClient.post<Int>(BASE_URL + saveGroup) {
                contentType(ContentType.Application.Json)
                header(AUTHORIZATION, sharedPreferences.getToken())
                body = group
            }

            group.id = response
            groupRepository.insert(group)
        }catch (ex:Exception){

        }
    }

    suspend fun getAllGroups(groupsListState: MutableState<Response<List<GroupListDto>>>) {
        groupRepository.getAllGroups(groupsListState)
        groupsListState.value = Response.loading(groupsListState.value.data)
        val token = sharedPreferences.getToken()
        val response = httpClient.get<GroupsAllDataDto>(BASE_URL + allGroups){
            header(AUTHORIZATION, token)
        }
        groupsListState.value = Response.success(response.data)

        response.data?.forEach { it ->
            groupRepository.insert(it.group)
        }
        try {

        }catch (ex: Exception){
            groupsListState.value = Response.error("Something went wrong $ex", groupsListState.value.data)
        }
    }
}
