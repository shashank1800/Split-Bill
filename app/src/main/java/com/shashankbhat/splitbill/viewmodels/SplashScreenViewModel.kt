package com.shashankbhat.splitbill.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashankbhat.splitbill.database.remote.entity.TokenDto
import com.shashankbhat.splitbill.database.remote.repository.LoginRepositoryRemote
import com.shashankbhat.splitbill.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val loginRepositoryRemote: LoginRepositoryRemote,
    val sharedPreferences: SharedPreferences,
    @ApplicationContext context: Context
) : ViewModel() {

    private var androidId : String?  =
        Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    var loginState : MutableLiveData<Response<TokenDto>> = MutableLiveData(Response.nothing())

    fun login() {
        viewModelScope.launch {
            loginRepositoryRemote.authentication(androidId, loginState)
        }
    }

    suspend fun ping(): Boolean {
        return loginRepositoryRemote.ping()
    }

}