package com.shashankbhat.splitbill.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.shashankbhat.splitbill.MainActivity
import com.shashankbhat.splitbill.ui.common.AppBar
import com.shashankbhat.splitbill.util.LoadingDialogFragment
import com.shashankbhat.splitbill.util.extension.findActivity

abstract class BaseFragment<ViewBind : ViewBinding> :Fragment() {

    private var previousTitle = ""
    private lateinit var loadingFragment : LoadingDialogFragment

    private var _binding: ViewBind? = null
    private lateinit var appBar: AppBar

    val binding: ViewBind
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        return binding.root
    }
    abstract fun getViewBinding(): ViewBind

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        previousTitle = (context?.findActivity() as MainActivity).title.get() ?: ""

        appBar = (context?.findActivity() as MainActivity).binding.appBar
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

    fun hideToolbar() {
        appBar.visibility = View.GONE
    }

    fun showToolbar() {
        appBar.visibility = View.VISIBLE
    }

    fun addMenuItem(menuItem : View){
        appBar.addMenuItem(menuItem)
    }

    fun clearMenu(){
        appBar.clearMenu()
    }
}