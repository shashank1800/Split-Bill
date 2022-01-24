package com.shashankbhat.splitbill.repository.remote.repository

import androidx.compose.runtime.MutableState
import com.shashankbhat.splitbill.repository.local.UserRepository
import com.shashankbhat.splitbill.repository.remote.entity.UsersAllDataDto
import com.shashankbhat.splitbill.room_db.entity.User
import com.shashankbhat.splitbill.ui.ApiConstants.BASE_URL
import com.shashankbhat.splitbill.ui.ApiConstants.getAllUser
import com.shashankbhat.splitbill.ui.ApiConstants.saveUser
import com.shashankbhat.splitbill.ui.ApiConstants.deleteUser
import com.shashankbhat.splitbill.util.Response
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class UserRepositoryRemote @Inject constructor(
    private val httpClient: HttpClient,
    private val userRepository: UserRepository
) {
    suspend fun insert(user: User?) {
        val response = httpClient.post<User>(BASE_URL + saveUser) {
            contentType(ContentType.Application.Json)
            body = user ?: {}
        }

        userRepository.insert(response)
        try {

        }catch (ex:Exception){

        }
    }


    suspend fun getAllUsersByGroupId(
        groupId: Int,
        userListState: MutableState<Response<List<User>>>
    ) {
        try {
            userRepository.getAllUsersByGroupId(groupId, userListState)
            val response = httpClient.get<UsersAllDataDto>(BASE_URL + getAllUser){
                parameter("groupId", groupId)
            }
            userListState.value = Response.success(response.data)

            response.data?.forEach { it ->
                userRepository.insert(it)
            }
        }catch (ex:Exception){

        }
    }

    suspend fun deleteUser(
        user: User?
    ) {
        val response = httpClient.put<User>(BASE_URL + deleteUser){
            contentType(ContentType.Application.Json)
            body = user ?: {}
        }
        if(response != null)
            userRepository.deleteUser(user)
        try {


        }catch (ex:Exception){

        }
    }
}
