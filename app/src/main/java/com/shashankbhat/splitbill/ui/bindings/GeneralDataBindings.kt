package com.shashankbhat.splitbill.ui.bindings

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import com.shashankbhat.splitbill.util.NetworkStatus
import com.shashankbhat.splitbill.util.extension.getColorV2
import com.shashankbhat.splitbill.util.extension.setImageByUrl

object GeneralDataBindings {

    @JvmStatic
    @BindingAdapter(value = ["bindImageViewByUrl"], requireAll = true)
    fun bindImageViewByUrl(imageView: ImageView, url: String?) {
        imageView.setImageByUrl(url)
    }

    @JvmStatic
    @BindingAdapter(value = ["bindImageViewByUrlAnimate"], requireAll = true)
    fun bindImageViewByUrlAnimate(imageView: ImageView, url: String?) {

        val fadeOut = AnimationUtils.loadAnimation(imageView.context, android.R.anim.fade_out)
        imageView.startAnimation(fadeOut)

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }
            override fun onAnimationEnd(animation: Animation) {
                imageView.setImageByUrl(url)
                val fadeIn = AnimationUtils.loadAnimation(imageView.context, android.R.anim.fade_in)
                imageView.startAnimation(fadeIn)

            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

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
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setProfileColorToCard"], requireAll = true)
    fun setProfileColorToCard(view: CardView, name: String){
        view.setCardBackgroundColor(name.getColorV2())
    }

}