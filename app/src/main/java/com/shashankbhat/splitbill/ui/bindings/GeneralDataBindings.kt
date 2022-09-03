package com.shashankbhat.splitbill.ui.bindings

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import com.bumptech.glide.Glide
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.util.NetworkStatus

object GeneralDataBindings {



    @JvmStatic
    @BindingAdapter(value = ["bindImageViewByUrl"], requireAll = true)
    fun bindImageViewByUrl(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
            .load(url)
            .centerCrop()
            .circleCrop()
            .placeholder(R.drawable.ic_outline_account_circle)
            .into(imageView)

    }

    @JvmStatic
    @BindingAdapter(value = ["bindNetworkStatus"], requireAll = true)
    fun bindNetworkStatus(view: View, networkStatus: ObservableField<NetworkStatus>?){
        when{
            networkStatus?.get()?.isAvailable() == true -> {
                view.visibility = View.GONE
            }

            networkStatus?.get()?.isUnavailable() == true -> {
                view.visibility = View.VISIBLE
            }

            else -> view.visibility = View.GONE
        }
    }

}