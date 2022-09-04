package com.shashankbhat.splitbill.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    fragments: List<Fragment>
) : FragmentStateAdapter(fragmentActivity) {

    var fragmentList: List<Fragment> = fragments

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun replaceFragmentsList(frgmts: List<Fragment>) {
        this.fragmentList = frgmts
    }
}
