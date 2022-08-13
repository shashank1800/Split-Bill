package com.shashankbhat.splitbill.database.remote.repository

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.shashankbhat.splitbill.BuildConfig.BASE_URL
import com.shashankbhat.splitbill.database.remote.entity.LocationDto
import com.shashankbhat.splitbill.database.remote.entity.NearUsersList
import com.shashankbhat.splitbill.model.NearUserModel
import com.shashankbhat.splitbill.ui.ApiConstants
import com.shashankbhat.splitbill.ui.ApiConstants.getNearUsers
import com.shashankbhat.splitbill.util.LatLong
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.extension.getToken
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
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
            val response = httpClient.get<NearUsersList>(BASE_URL + getNearUsers) {
                header(ApiConstants.AUTHORIZATION, sharedPreferences.getToken())
                contentType(ContentType.Application.Json)
                body = LocationDto(location.latitude, location.longitude)
            }

            val listData = arrayListOf<NearUserModel>()

            response.users.forEach {
                listData.add(NearUserModel(it.uniqueId ?: -1, it.name, it.photoUrl, it.latitude, it.longitude))
            }

            nearUserList.value = Response.success(listData)

        }catch (ex: Exception){
            println(ex)
            nearUserList.value = Response.error(ex.message)
        }

    }
}