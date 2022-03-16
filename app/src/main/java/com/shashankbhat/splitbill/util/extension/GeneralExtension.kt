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

fun SharedPreferences.getLocalId(): Int {

    val editor = this.edit()

    val key = "local_id"
    val keyCount = "keyCount"

    editor.putInt(key, getInt(key, 0) - 1)
    editor.putInt(keyCount, getInt(keyCount, 0) + 1)
    editor.apply()

    return this.getInt("local_id", -1)
}

fun SharedPreferences.releaseOne(): Int {

    val key = "local_id"
    val keyCount = "keyCount"

    val editor = this.edit()
    val count = getInt(keyCount, 0) - 1
    editor.putInt(keyCount,  count)

    if(count == 0)
        editor.putInt(key, 0)
    editor.apply()

    return this.getInt("local_id", -1)
}