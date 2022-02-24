package com.shashankbhat.splitbill.repository.remote.repository

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import com.shashankbhat.splitbill.repository.remote.entity.LoginDto
import com.shashankbhat.splitbill.repository.remote.entity.TokenDto
import com.shashankbhat.splitbill.ui.ApiConstants.BASE_URL
import com.shashankbhat.splitbill.ui.ApiConstants.authentication
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.extension.putToken
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class LoginRepositoryRemote @Inject constructor(
    private val httpClient: HttpClient,
    private val sharedPreferences: SharedPreferences
){
    suspend fun authentication(androidId: String?, loginState: MutableLiveData<Response<TokenDto>>) {

        val response = httpClient.post<TokenDto>(BASE_URL + authentication) {
            contentType(ContentType.Application.Json)
            body = LoginDto(androidId, androidId)
        }
        sharedPreferences.putToken(response.token)

        loginState.value = Response.success(response)
        try {

        }catch (ex:Exception){

        }
    }
}