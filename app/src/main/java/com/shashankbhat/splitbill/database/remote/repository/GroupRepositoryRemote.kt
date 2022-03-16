package com.shashankbhat.splitbill.database.remote.repository

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.database.local.repository.GroupRepository
import com.shashankbhat.splitbill.database.remote.entity.GroupsAllDataDto
import com.shashankbhat.splitbill.database.local.entity.Groups
import com.shashankbhat.splitbill.ui.ApiConstants.AUTHORIZATION
import com.shashankbhat.splitbill.ui.ApiConstants.BASE_URL
import com.shashankbhat.splitbill.ui.ApiConstants.allGroups
import com.shashankbhat.splitbill.ui.ApiConstants.saveGroup
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.extension.getLocalId
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
    suspend fun insert(group: Groups, addLocalCallback:(isAdded: Boolean) -> Unit) {
        try {
            group.id = sharedPreferences.getLocalId()
            group.isUploaded = false

            groupRepository.insert(group)
            addLocalCallback(true)

            val response = httpClient.post<Int>(BASE_URL + saveGroup) {
                contentType(ContentType.Application.Json)
                header(AUTHORIZATION, sharedPreferences.getToken())
                body = group
            }
            val localId = group.id ?: 0
            group.isUploaded = true
            groupRepository.update(localId, response)
        }catch (ex:Exception){
        }
    }

    suspend fun getAllGroups(groupsListState: MutableState<Response<List<GroupListDto>>>) {

        try {
            groupRepository.getAllGroups(groupsListState)
            groupsListState.value = Response.loading(groupsListState.value.data)
            val token = sharedPreferences.getToken()
            val response = httpClient.get<GroupsAllDataDto>(BASE_URL + allGroups){
                header(AUTHORIZATION, token)
            }
            groupsListState.value = Response.success(response.data)

            response.data?.forEach { it ->
                it.group.isUploaded = true
                groupRepository.insert(it.group)
            }
        }
//        catch (ce: ClientRequestException){
//            groupsListState.value = Response.unauthorized("Please restart app")
//        }
        catch (ex: Exception){
            groupsListState.value = Response.error("Something went wrong $ex", groupsListState.value.data)
        }
    }
}
