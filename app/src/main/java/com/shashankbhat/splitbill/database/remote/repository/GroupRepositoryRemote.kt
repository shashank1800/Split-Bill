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
import com.shashankbhat.splitbill.util.DatabaseOperation
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.extension.getLocalId
import com.shashankbhat.splitbill.util.extension.getToken
import com.shashankbhat.splitbill.util.extension.getUniqueId
import com.shashankbhat.splitbill.util.extension.releaseOne
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class GroupRepositoryRemote @Inject constructor(
    private val httpClient: HttpClient,
    private val groupRepository: GroupRepository,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun insert(group: Groups, addLocalCallback: (type: DatabaseOperation) -> Unit) {
        try {
            group.id = sharedPreferences.getLocalId()
            group.uniqueId = sharedPreferences.getUniqueId()
            groupRepository.insert(group)
            addLocalCallback(DatabaseOperation.LOCAL)

            val remoteId = httpClient.post<Int>(BASE_URL + saveGroup) {
                contentType(ContentType.Application.Json)
                header(AUTHORIZATION, sharedPreferences.getToken())
                body = group
            }

            val localId = group.id ?: 0
            groupRepository.update(localId, remoteId)
            addLocalCallback(DatabaseOperation.REMOTE)
            sharedPreferences.releaseOne()
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
