package com.shashankbhat.splitbill.database.remote.repository

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import com.shashankbhat.splitbill.database.local.repository.UserRepository
import com.shashankbhat.splitbill.database.remote.entity.UsersAllDataDto
import com.shashankbhat.splitbill.database.local.entity.User
import com.shashankbhat.splitbill.ui.ApiConstants
import com.shashankbhat.splitbill.ui.ApiConstants.BASE_URL
import com.shashankbhat.splitbill.ui.ApiConstants.getAllUser
import com.shashankbhat.splitbill.ui.ApiConstants.saveUser
import com.shashankbhat.splitbill.ui.ApiConstants.deleteUser
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.extension.getLocalId
import com.shashankbhat.splitbill.util.extension.getToken
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class UserRepositoryRemote @Inject constructor(
    private val httpClient: HttpClient,
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun insert(user: User?, addLocalCallback:(isAdded: Boolean)-> Unit) {

        try {
            user?.id = sharedPreferences.getLocalId()
            user?.isUploaded = false

            userRepository.insert(user)
            addLocalCallback(true)

            val response = httpClient.post<User>(BASE_URL + saveUser) {
                contentType(ContentType.Application.Json)
                header(ApiConstants.AUTHORIZATION, sharedPreferences.getToken())
                body = user ?: {}
            }

            val localId = user?.id ?: 0
            user?.isUploaded = true

            userRepository.update(localId, response.id ?: 0)
        }catch (ex:Exception){

        }
    }


    suspend fun getAllUsersByGroupId(
        groupId: Int,
        userListState: MutableState<Response<List<User>>>? = null
    ): List<User>? {
        try {

            if(userListState == null)
                return userRepository.getAllUsersByGroupId(groupId)

            userListState.let {
                userRepository.getAllUsersByGroupId(groupId, userListState)
            }

            val response = httpClient.get<UsersAllDataDto>(BASE_URL + getAllUser){
                header(ApiConstants.AUTHORIZATION, sharedPreferences.getToken())
                parameter("groupId", groupId)
            }
            userListState.value = Response.success(response.data)

            response.data?.forEach { it ->
                userRepository.insert(it)
            }
        }catch (ex:Exception){

        }
        return null
    }

    suspend fun deleteUser(
        user: User?, addLocalCallback:(isDeleted: Boolean)-> Unit
    ) {

        try {
            userRepository.deleteUser(user)
            addLocalCallback(true)

            val response = httpClient.put<User>(BASE_URL + deleteUser){
                contentType(ContentType.Application.Json)
                header(ApiConstants.AUTHORIZATION, sharedPreferences.getToken())
                body = user ?: {}
            }


        }catch (ex:Exception){

        }
    }
}
