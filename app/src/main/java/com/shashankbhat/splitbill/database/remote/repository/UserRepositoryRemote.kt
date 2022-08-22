package com.shashankbhat.splitbill.database.remote.repository

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.shashankbhat.splitbill.database.local.dto.users.UsersLinkDto
import com.shashankbhat.splitbill.database.local.repository.UserRepository
import com.shashankbhat.splitbill.database.remote.entity.UsersAllDataDto
import com.shashankbhat.splitbill.database.local.entity.User
import com.shashankbhat.splitbill.ui.ApiConstants
import com.shashankbhat.splitbill.BuildConfig.BASE_URL
import com.shashankbhat.splitbill.database.local.dto.profile.UpdateProfilePhotoDto
import com.shashankbhat.splitbill.database.remote.entity.SaveProfileDto
import com.shashankbhat.splitbill.ui.ApiConstants.getAllUser
import com.shashankbhat.splitbill.ui.ApiConstants.saveUser
import com.shashankbhat.splitbill.ui.ApiConstants.deleteUser
import com.shashankbhat.splitbill.ui.ApiConstants.linkUser
import com.shashankbhat.splitbill.ui.ApiConstants.profileDetail
import com.shashankbhat.splitbill.ui.ApiConstants.saveProfile
import com.shashankbhat.splitbill.ui.ApiConstants.updateProfilePhoto
import com.shashankbhat.splitbill.util.DatabaseOperation
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.extension.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class UserRepositoryRemote @Inject constructor(
    private val httpClient: HttpClient,
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun insert(user: User?, databaseOperation:(type: DatabaseOperation)-> Unit) {

        try {
            user?.id = sharedPreferences.getLocalId()

            userRepository.insert(user)
            databaseOperation(DatabaseOperation.LOCAL)

            val response = httpClient.post<User>(BASE_URL + saveUser) {
                contentType(ContentType.Application.Json)
                header(ApiConstants.AUTHORIZATION, sharedPreferences.getToken())
                body = user ?: {}
            }

            val localId = user?.id ?: 0
            userRepository.update(localId, response.id ?: 0)
            databaseOperation(DatabaseOperation.REMOTE)
            sharedPreferences.releaseOne()
        }catch (ex:Exception){

        }
    }


    suspend fun getAllUsersByGroupId(
        groupId: Int,
        userListState: MutableLiveData<Response<List<User>>>? = null
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
        user: User?, databaseOperation:(type: DatabaseOperation) -> Unit
    ) {

        try {
            userRepository.deleteUser(user)
            databaseOperation(DatabaseOperation.LOCAL)

            val response = httpClient.put<User>(BASE_URL + deleteUser){
                contentType(ContentType.Application.Json)
                header(ApiConstants.AUTHORIZATION, sharedPreferences.getToken())
                body = user ?: {}
            }
        }catch (ex:Exception){
        }
    }

    suspend fun linkUser(id: Int?, uniqueId: String?, databaseOperation:(type: DatabaseOperation)-> Unit) {

        try {

            val response = httpClient.put<UsersLinkDto>(BASE_URL + linkUser) {
                contentType(ContentType.Application.Json)
                header(ApiConstants.AUTHORIZATION, sharedPreferences.getToken())
                body = UsersLinkDto(id, uniqueId?.toInt())
            }
        }catch (ex:Exception){}
    }

    suspend fun saveProfile(name: String? = null, photoUrl: String? = null, isNearbyVisible: Boolean? = null, distanceRange: Double? = null): Response<String> {

        try {
            httpClient.post<Int>(BASE_URL + saveProfile) {
                contentType(ContentType.Application.Json)
                header(ApiConstants.AUTHORIZATION, sharedPreferences.getToken())
                body = SaveProfileDto(name, photoUrl, isNearbyVisible, distanceRange)
            }

            sharedPreferences.putFullName(name ?:"")
            sharedPreferences.putPhotoUrl(photoUrl ?:"")
            sharedPreferences.putIsNearVisible(isNearbyVisible ?: false)
            sharedPreferences.putDistanceRange(distanceRange ?: 0.0)
            return Response.success("SUCCESS")
        }catch (ex:Exception){
            print(ex)
            return Response.error(ex.message)
        }
    }

    suspend fun getProfile(): Response<SaveProfileDto> {

        try {
            val result = httpClient.get<SaveProfileDto>(BASE_URL + profileDetail) {
                contentType(ContentType.Application.Json)
                header(ApiConstants.AUTHORIZATION, sharedPreferences.getToken())
            }

            sharedPreferences.putFullName(result.name ?:"")
            sharedPreferences.putPhotoUrl(result.photoUrl ?:"")
            sharedPreferences.putIsNearVisible(result.isNearbyVisible ?: false)
            sharedPreferences.putDistanceRange(result.distanceRange ?: 0.0)

            return Response.success(result)
        }catch (ex:Exception){
            print(ex)
            return Response.error(ex.message)
        }
    }

    suspend fun updateProfilePhoto(
        photoUrl: String? = null
    ): Response<String> {

        try {
            val id = httpClient.put<Int?>(BASE_URL + updateProfilePhoto) {
                contentType(ContentType.Application.Json)
                header(ApiConstants.AUTHORIZATION, sharedPreferences.getToken())
                body = UpdateProfilePhotoDto(photoUrl)
            }

            if(id != null){
                sharedPreferences.putPhotoUrl(photoUrl ?:"")
                return Response.success(photoUrl)
            }
        }catch (ex:Exception){
            print(ex)
            return Response.error(ex.message)
        }
        return Response.nothing()
    }

}
