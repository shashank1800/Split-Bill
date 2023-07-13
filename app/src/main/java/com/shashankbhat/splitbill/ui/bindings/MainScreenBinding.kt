package com.shashankbhat.splitbill.ui.bindings

import android.content.SharedPreferences
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shahankbhat.recyclergenericadapter.util.DataBinds
import com.shahankbhat.recyclergenericadapter.util.MoreDataBindings
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.databinding.AdapterGroupUsersProfileBinding
import com.shashankbhat.splitbill.database.local.model.NearUserModel
import com.shashankbhat.splitbill.util.LatLong
import com.shashankbhat.splitbill.BR
import com.shashankbhat.splitbill.database.local.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.database.local.dto.bill_shares.BillSharesModel
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupRecyclerListDto
import com.shashankbhat.splitbill.database.local.dto.users.UserDto
import com.shashankbhat.splitbill.databinding.AdapterBillShareBillBinding
import com.shashankbhat.splitbill.di.AppModule.providesSharedPreference
import com.shashankbhat.splitbill.util.RecyclerItemOverlap
import com.shashankbhat.splitbill.util.extension.*
import com.shashankbhat.splitbill.util.extension.getTimeAgo

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
            cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, R.color.common_card_selected_background_color))
        } else {
            cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, R.color.common_card_view_background_color))
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["bindUsersList"], requireAll = true)
    fun bindUsersList(recyclerView: RecyclerView, groupListDto: GroupRecyclerListDto) {
        val sharedPref = providesSharedPreference(recyclerView.context.applicationContext)
        val adapter = if(groupListDto.adapter != null) groupListDto.adapter else
            RecyclerGenericAdapter.Builder<AdapterGroupUsersProfileBinding, UserDto>(R.layout.adapter_group_users_profile, BR.model)
                .setMoreDataBinds(DataBinds(arrayListOf<MoreDataBindings?>().apply {
                    add(MoreDataBindings(BR.sharedPref, sharedPref))
                }))
                .build()
        recyclerView.addItemDecoration(RecyclerItemOverlap(left = -15))
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = adapter
        adapter?.replaceList(ArrayList(groupListDto.userList?.take(3) ?: emptyList()))
        groupListDto.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter(value = ["bindProfileColor"], requireAll = true)
    fun bindProfileColor(cardView: CardView, name: String) {
        cardView.setCardBackgroundColor(name.getColorV2())
    }

    @JvmStatic
    @BindingAdapter(value = ["bindMilliSecondsToAgoTime"], requireAll = true)
    fun bindMilliSecondsToAgoTime(textView: TextView, time: Long) {
        textView.text = textView.context.getTimeAgo(time)
    }

    @JvmStatic
    @BindingAdapter(value = ["bindBillShareBillList"], requireAll = true)
    fun bindBillShareBillList(recyclerView: RecyclerView, billModel: BillModel) {
        val sharedPref = providesSharedPreference(recyclerView.context.applicationContext)
        billModel.billShares?.let {
            val adapter = RecyclerGenericAdapter.Builder<AdapterBillShareBillBinding, BillSharesModel>(R.layout.adapter_bill_share_bill, BR.model)
                .setMoreDataBinds(DataBinds(arrayListOf<MoreDataBindings?>().apply {
                    add(MoreDataBindings(BR.sharedPref, sharedPref))
                }))
                .build()
            (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.adapter = adapter
            adapter.replaceList(ArrayList(it))
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setRefreshing"], requireAll = true)
    fun setRefreshing(srl: SwipeRefreshLayout, isRefreshing: Boolean) {
        srl.isRefreshing = isRefreshing
    }


}