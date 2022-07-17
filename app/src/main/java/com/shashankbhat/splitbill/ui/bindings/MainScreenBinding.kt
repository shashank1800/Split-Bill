package com.shashankbhat.splitbill.ui.bindings

import android.content.SharedPreferences
import android.graphics.Color
import android.text.format.DateUtils.*
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shahankbhat.recyclergenericadapter.util.DataBinds
import com.shahankbhat.recyclergenericadapter.util.MoreDataBindings
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.databinding.AdapterGroupUsersProfileBinding
import com.shashankbhat.splitbill.model.NearUserModel
import com.shashankbhat.splitbill.util.LatLong
import com.shashankbhat.splitbill.BR
import com.shashankbhat.splitbill.database.local.entity.User
import com.shashankbhat.splitbill.util.RecyclerItemOverlap
import com.shashankbhat.splitbill.util.extension.*

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

    @JvmStatic
    @BindingAdapter(value = ["bindUsersList"], requireAll = true)
    fun bindUsersList(recyclerView: RecyclerView, groupListDto: GroupListDto) {
        if (groupListDto.userList.isNotEmpty()) {
            val adapter = RecyclerGenericAdapter.Builder<AdapterGroupUsersProfileBinding, User>(R.layout.adapter_group_users_profile, BR.model)
                .build()
            recyclerView.addItemDecoration(RecyclerItemOverlap(left = -20))
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, RecyclerView.HORIZONTAL, false)
            recyclerView.adapter = adapter
            adapter.replaceList(ArrayList(groupListDto.userList.take(3)))
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["bindProfileColor"], requireAll = true)
    fun bindProfileColor(cardView: CardView, name: String) {
        cardView.setCardBackgroundColor(name.getColorV2())
    }

    @JvmStatic
    @BindingAdapter(value = ["bindMilliSecondsToAgoTime"], requireAll = true)
    fun bindMilliSecondsToAgoTime(textView: TextView, time: Long) {
        textView.text = getRelativeDateTimeString(
            textView.context, time,
            MINUTE_IN_MILLIS,
            WEEK_IN_MILLIS,
            FORMAT_SHOW_TIME
        ).toString()
    }


}