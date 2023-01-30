package com.shashankbhat.splitbill.ui.bill_shares

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.base.BaseFragment
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.databinding.FragmentBillShareViewPagerBinding
import com.shashankbhat.splitbill.enums.SnackBarType
import com.shashankbhat.splitbill.util.ViewPagerAdapter
import com.shashankbhat.splitbill.util.extension.showSnackBar
import com.shashankbhat.splitbill.viewmodels.BillShareViewModel

class BillShareViewPager : BaseFragment<FragmentBillShareViewPagerBinding>() {

    private var groupListDto: GroupListDto? = null

    private val viewModel: BillShareViewModel by activityViewModels()
    private val adapterFragments = arrayListOf<Fragment>()
    private val args: BillShareViewPagerArgs by navArgs()

    override fun getViewBinding() = FragmentBillShareViewPagerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupListDto = args.model
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.isBillListEmpty = viewModel.isBillListEmpty

        uiViewPageInit()
        uiBottomNavigationClickListener()
    }

    private fun uiViewPageInit() {
        val billSharesFragment = BillShareFragment.getInstance(args)
        val balanceFragment = ShowBillSharesBalanceFragment.getInstance(args)
        adapterFragments.add(billSharesFragment)
        adapterFragments.add(balanceFragment)

        binding.vpBillShares.adapter = ViewPagerAdapter(requireActivity(), adapterFragments)
        binding.vpBillShares.isUserInputEnabled = false
    }

    private fun uiBottomNavigationClickListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_bill_shares -> binding.vpBillShares.setCurrentItem(0, true)
                R.id.menu_balances -> {
                    binding.vpBillShares.setCurrentItem(1, true)
                }
                R.id.menu_statistics -> {
                    binding.showSnackBar(
                        "Work in progress",
                        "Okay",
                        snackBarType = SnackBarType.INSTRUCTION
                    )
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }

}