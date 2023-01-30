package com.shashankbhat.splitbill.database.remote.repository

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.shashankbhat.splitbill.database.remote.entity.LoginDto
import com.shashankbhat.splitbill.database.remote.entity.TokenDto
import com.shashankbhat.splitbill.BuildConfig.BASE_URL
import com.shashankbhat.splitbill.ui.ApiConstants.authentication
import com.shashankbhat.splitbill.ui.ApiConstants.ping
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.extension.putToken
import com.shashankbhat.splitbill.util.extension.putUniqueId
import com.shashankbhat.splitbill.util.extension.putUsername
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class LoginRepositoryRemote @Inject constructor(
    private val httpClient: HttpClient,
    private val sharedPreferences: SharedPreferences
){
    suspend fun authentication(androidId: String?, loginState: MutableLiveData<Response<TokenDto>>) {

        try {
            val response = httpClient.post<TokenDto>(BASE_URL + authentication) {
                contentType(ContentType.Application.Json)
                body = LoginDto(androidId, androidId)
            }
            response.token?.let { sharedPreferences.putToken(it) }
            response.uniqueId?.let { sharedPreferences.putUniqueId(it) }
            response.username?.let { sharedPreferences.putUsername(it) }

            loginState.postValue(Response.success(response))
        }catch (ex:Exception){
            print(ex)
        }
    }

    suspend fun ping(): Boolean {
        return try {
            val response = httpClient.get<Boolean>(BASE_URL + ping)
            response
        }catch (ex:Exception){
            false
        }
    }
}