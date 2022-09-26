package com.shashankbhat.splitbill.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.shashankbhat.splitbill.MainActivity
import com.shashankbhat.splitbill.util.LoadingDialogFragment
import com.shashankbhat.splitbill.util.extension.findActivity

open class BaseFragment :Fragment() {

    private var previousTitle = ""
    private lateinit var loadingFragment : LoadingDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        previousTitle = (context?.findActivity() as MainActivity).title.get() ?: ""
    }

    fun setTitle(title: String){
        (context?.findActivity() as MainActivity).title.set(title)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    fun showLoading() {
        if(!::loadingFragment.isInitialized)
            loadingFragment = LoadingDialogFragment()
        loadingFragment.show(parentFragmentManager, loadingFragment.tag)
    }

    fun hideLoading() {
        if(::loadingFragment.isInitialized)
            loadingFragment.dismiss()
    }
}