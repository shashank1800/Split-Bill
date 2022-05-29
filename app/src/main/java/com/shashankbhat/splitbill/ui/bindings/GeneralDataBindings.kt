package com.shashankbhat.splitbill.ui.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.shashankbhat.splitbill.R

object GeneralDataBindings {



    @JvmStatic
    @BindingAdapter(value = ["bindImageViewByUrl"], requireAll = true)
    fun bindImageViewByUrl(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.ic_outline_account_circle)
            .into(imageView)

    }

}