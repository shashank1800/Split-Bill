package com.shashankbhat.splitbill.util.extension

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

fun Context.findActivity(): AppCompatActivity? = when (this) {
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun SharedPreferences.putToken(token : String){
    val editor = this.edit()
    editor.putString("token", "Bearer $token")
    editor.apply()
}

fun SharedPreferences.getToken(): String {
    return this.getString("token", "") ?: ""
}