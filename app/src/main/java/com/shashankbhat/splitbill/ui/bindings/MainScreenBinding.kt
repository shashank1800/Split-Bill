package com.shashankbhat.splitbill.ui.bindings

import android.content.SharedPreferences
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.shashankbhat.splitbill.database.remote.entity.NearUserDto
import com.shashankbhat.splitbill.util.LatLong
import com.shashankbhat.splitbill.util.extension.findDistance
import com.shashankbhat.splitbill.util.extension.format
import com.shashankbhat.splitbill.util.extension.getLocation

object MainScreenBinding {

    @JvmStatic
    @BindingAdapter(value = ["bindNearUserDistance", "bindSharedPreference"], requireAll = true)
    fun bindDateMillisToString(textView: TextView, model: NearUserDto, sharedPreferences: SharedPreferences) {
        if (model.latitude != null && model.longitude != null) {
            val locationFrom = sharedPreferences.getLocation()
            textView.text = "${locationFrom.findDistance(LatLong(model.latitude, model.longitude)).format(2)} km"
        } else {
            textView.text = ""
        }
    }
}