package com.shashankbhat.splitbill.util.extension

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout

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

fun SharedPreferences.putUniqueId(uniqueId : Int){
    val editor = this.edit()
    editor.putInt("uniqueId", uniqueId)
    editor.apply()
}

fun SharedPreferences.getUniqueId(): Int {
    return this.getInt("uniqueId", 0)
}

fun SharedPreferences.putUsername(username : String){
    val editor = this.edit()
    editor.putString("username", username)
    editor.apply()
}

fun SharedPreferences.getUsername(): String {
    return this.getString("username", "") ?: ""
}

fun String.getColor(): Color{

    var sum = 0

    this.forEach { character ->
        sum += character.code
    }

    return Color(android.graphics.Color.parseColor(colors[sum % colors.size]))
}

fun Modifier.badgeLayout() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        // based on the expectation of only one line of text
        val minPadding = placeable.height / 4

        val width = maxOf(placeable.width + minPadding, placeable.height)
        layout(width, placeable.height) {
            placeable.place((width - placeable.width) / 2, 0)
        }
    }

val colors = listOf(
    "#2ab7ca",
    "#005b96",
    "#fed766",
    "#f6abb6",
    "#03396c",
    "#b3cde0",
    "#051e3e",
    "#251e3e",
    "#451e3e",
    "#651e3e",
    "#851e3e",
    "#009688",
    "#35a79c",
    "#854442",
    "#7f8e9e",
    "#343d46",
    "#4f5b66",
    "#a16ae8",
    "#04d4f0"
)