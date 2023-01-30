package com.shashankbhat.splitbill.ui.main_ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.base.BaseFragment
import com.shashankbhat.splitbill.databinding.FragmentHomeScreenViewPagerBinding
import com.shashankbhat.splitbill.databinding.LayoutMenuItemBinding
import com.shashankbhat.splitbill.ui.main_ui.group_list.GroupListFragment
import com.shashankbhat.splitbill.ui.main_ui.nearby_people.NearbyPeopleFragment
import com.shashankbhat.splitbill.ui.main_ui.profile.ProfileFragment
import com.shashankbhat.splitbill.util.ViewPagerAdapter
import com.shashankbhat.splitbill.util.extension.getUniqueId
import com.shashankbhat.splitbill.util.extension.observeNetworkStatus
import com.shashankbhat.splitbill.viewmodels.MainScreenViewModel

class HomeScreenViewPager : BaseFragment<FragmentHomeScreenViewPagerBinding>() {

    private val viewModel: MainScreenViewModel by activityViewModels()

    override fun getViewBinding() = FragmentHomeScreenViewPagerBinding.inflate(layoutInflater)

    override fun onStart() {
        super.onStart()

        val menuItem = LayoutMenuItemBinding.inflate(LayoutInflater.from(requireContext()))
        menuItem.tvTitle.text = "# ${viewModel.sharedPreferences.getUniqueId()}"
        addMenuItem(menuItem.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.vpBillShares = binding.vpBillShares
        binding.observeNetworkStatus = observeNetworkStatus()

        uiViewPagerInit()
        uiViewPagerScrollListener()
        uiBottomNavigationClickListener()
        uiNetworkSettingClickListener()
        uiOnBackPressed()
    }

    private fun uiViewPagerInit(){
        val groupsFragment = GroupListFragment.getInstance()
        val peopleFragment = NearbyPeopleFragment.getInstance()
        val profileFragment = ProfileFragment.getInstance()

        val adapterFragments = arrayListOf<Fragment>()
        adapterFragments.add(groupsFragment)
        adapterFragments.add(peopleFragment)
        adapterFragments.add(profileFragment)

        val adapter = ViewPagerAdapter(requireActivity(), adapterFragments)
        binding.vpBillShares.adapter = adapter
    }

    private fun uiViewPagerScrollListener(){
        binding.vpBillShares.isUserInputEnabled = false
    }

    private fun uiBottomNavigationClickListener(){
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_groups -> binding.vpBillShares.setCurrentItem(0, true)
                R.id.menu_people -> binding.vpBillShares.setCurrentItem(1, true)
                R.id.menu_profile -> {
                    viewModel.getProfile()
                    binding.vpBillShares.setCurrentItem(2, true)
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun uiNetworkSettingClickListener(){
        binding.layoutNetworkStatus.btnSetting.setOnClickListener{
            startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllGroups()
    }

    override fun onStop() {
        super.onStop()
        clearMenu()
    }

    private fun uiOnBackPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val index = binding.vpBillShares.currentItem

                if(index == 0)
                    requireActivity().finish()
                else {
                    binding.vpBillShares.setCurrentItem(0, true)
                    binding.bottomNavigation.selectedItemId = R.id.menu_groups
                }
            }
        })
    }

}