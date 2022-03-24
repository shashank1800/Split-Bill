package com.shashankbhat.splitbill.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.shashankbhat.splitbill.util.extension.findActivity

open class TitleFragment :Fragment() {

    private var previousTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        previousTitle = context?.findActivity()?.supportActionBar?.title.toString()
    }

    fun setTitle(title: String){
        context?.findActivity()?.supportActionBar?.title = title
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.findActivity()?.supportActionBar?.title = previousTitle
    }
}