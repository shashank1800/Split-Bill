package com.shashankbhat.splitbill.database.remote.repository

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.shashankbhat.splitbill.BuildConfig.BASE_URL
import com.shashankbhat.splitbill.database.remote.entity.LocationDto
import com.shashankbhat.splitbill.database.remote.entity.NearUsersList
import com.shashankbhat.splitbill.database.local.model.NearUserModel
import com.shashankbhat.splitbill.ui.ApiConstants
import com.shashankbhat.splitbill.ui.ApiConstants.getNearUsers
import com.shashankbhat.splitbill.util.KnownException
import com.shashankbhat.splitbill.util.LatLong
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.extension.getToken
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class LocationRepositoryRemote @Inject constructor(
    private val httpClient: HttpClient,
    private val sharedPreferences: SharedPreferences
){
    suspend fun getNearUser(
        location: LatLong,
        nearUserList: MutableLiveData<Response<ArrayList<NearUserModel>>>
    ) {
        try {
            val response: NearUsersList = httpClient.get(BASE_URL + getNearUsers) {
                header(ApiConstants.AUTHORIZATION, sharedPreferences.getToken())
                contentType(ContentType.Application.Json)
                body = LocationDto(location.latitude, location.longitude)
            }

            val listData = arrayListOf<NearUserModel>()

            response.users.forEach {
                listData.add(NearUserModel(it.uniqueId ?: -1, it.name, it.photoUrl, it.latitude, it.longitude))
            }

            nearUserList.value = Response.success(listData)

        }catch (ke: KnownException) {
            nearUserList.value = Response.error(ke.errorMessage)
        }catch (ex: Exception){
            nearUserList.value = Response.nothing()
        }

    }
}