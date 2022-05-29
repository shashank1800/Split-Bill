package com.shashankbhat.splitbill.database.remote.repository

import android.content.SharedPreferences
import com.shashankbhat.splitbill.BuildConfig.BASE_URL
import com.shashankbhat.splitbill.database.remote.entity.LocationDto
import com.shashankbhat.splitbill.database.remote.entity.NearUsersList
import com.shashankbhat.splitbill.ui.ApiConstants
import com.shashankbhat.splitbill.ui.ApiConstants.getNearUsers
import com.shashankbhat.splitbill.util.LatLong
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
        nearUserList: MutableStateFlow<NearUsersList>
    ) {
        try {
            val response = httpClient.get<NearUsersList>(BASE_URL + getNearUsers) {
                header(ApiConstants.AUTHORIZATION, sharedPreferences.getToken())
                contentType(ContentType.Application.Json)
                body = LocationDto(location.latitude, location.longitude)
            }

            nearUserList.emit(response)
        }catch (ex: Exception){
            println(ex)
        }

    }
}