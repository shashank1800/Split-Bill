package com.shashankbhat.splitbill.ui.bindings

import android.content.SharedPreferences
import android.graphics.Color
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.shashankbhat.splitbill.model.NearUserModel
import com.shashankbhat.splitbill.util.LatLong
import com.shashankbhat.splitbill.util.extension.findDistance
import com.shashankbhat.splitbill.util.extension.format
import com.shashankbhat.splitbill.util.extension.getLocation

object MainScreenBinding {

    @JvmStatic
    @BindingAdapter(value = ["bindNearUserDistance", "bindSharedPreference"], requireAll = true)
    fun bindDateMillisToString(textView: TextView, model: NearUserModel, sharedPreferences: SharedPreferences) {
        if (model.latitude != null && model.longitude != null) {
            val locationFrom = sharedPreferences.getLocation()
            textView.text = "${locationFrom.findDistance(LatLong(model.latitude ?: 0.0, model.longitude ?: 0.0))
                .format(2)} km"
        } else {
            textView.text = ""
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["bindSelectedCardView"], requireAll = true)
    fun bindSelectedCardView(cardView: CardView, isSelected: Boolean) {

        if (isSelected) {
            cardView.setCardBackgroundColor(Color.parseColor("#DDF0FF"))
        } else {
            cardView.setCardBackgroundColor(Color.WHITE)
        }
    }
}