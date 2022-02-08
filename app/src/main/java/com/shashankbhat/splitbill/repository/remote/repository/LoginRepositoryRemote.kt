package com.shashankbhat.splitbill.repository.remote.repository

import android.content.SharedPreferences
import com.shashankbhat.splitbill.repository.remote.entity.LoginDto
import com.shashankbhat.splitbill.repository.remote.entity.TokenDto
import com.shashankbhat.splitbill.ui.ApiConstants.BASE_URL
import com.shashankbhat.splitbill.ui.ApiConstants.authentication
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class LoginRepositoryRemote @Inject constructor(
    private val httpClient: HttpClient,
    private val sharedPreferences: SharedPreferences
){
    suspend fun authentication() {

        try {
            val response = httpClient.post<TokenDto>(BASE_URL + authentication) {
                contentType(ContentType.Application.Json)
                body = LoginDto("", "") ?: {}
            }
        }catch (ex:Exception){

        }
    }
}