package com.shashankbhat.splitbill.util.extension

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.shashankbhat.splitbill.util.LatLong

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


fun SharedPreferences.setLocation(location : LatLong){
    val editor = this.edit()
    editor.putString("location.latitude", location.latitude.toString())
    editor.putString("location.longitude", location.longitude.toString())
    editor.apply()
}

fun SharedPreferences.getLocation(): LatLong {
    val latitude = this.getString("location.latitude", "0.0")?.toDouble()
    val longitude = this.getString("location.longitude", "0.0")?.toDouble()
    return LatLong(latitude ?: 0.0, longitude ?: 0.0)
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)

fun SharedPreferences.putFullName(value : String){
    val editor = this.edit()
    editor.putString("full_name", value)
    editor.apply()
}

fun SharedPreferences.getFullName(): String = this.getString("full_name", "") ?: ""

fun SharedPreferences.putPhotoUrl(value : String){
    val editor = this.edit()
    editor.putString("photo_url", value)
    editor.apply()
}

fun SharedPreferences.getPhotoUrl(): String = this.getString("photo_url", "") ?: ""

fun SharedPreferences.putDistanceRange(value : Double){
    val editor = this.edit()
    editor.putString("distance_range", value.toString())
    editor.apply()
}

fun SharedPreferences.getDistanceRange(): Double = this.getString("distance_range", "0.0")?.toDouble() ?: 0.0

fun SharedPreferences.putIsNearVisible(value : Boolean){
    val editor = this.edit()
    editor.putBoolean("is_near_visible", value)
    editor.apply()
}

fun SharedPreferences.getIsNearVisible(): Boolean = this.getBoolean("is_near_visible", false)


fun SharedPreferences.getProfileIcons(): List<String> = listOf(
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

fun <T : ViewDataBinding> T.showSnackBar(message: String, action: String? = null, duration: Int = Snackbar.LENGTH_SHORT,
                                         actionListener: View.OnClickListener? = null) {
    val snackBar = Snackbar.make(this.root, message, duration)
        .setBackgroundTint(android.graphics.Color.parseColor("#FF3EC590"))
        .setTextColor(android.graphics.Color.WHITE)
    if (action != null && actionListener!=null) {
        snackBar.setAction(action, actionListener)
    }
    snackBar.show()
}