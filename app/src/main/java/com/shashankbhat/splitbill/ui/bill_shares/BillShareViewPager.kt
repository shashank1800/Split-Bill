package com.shashankbhat.splitbill.ui.bill_shares

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.databinding.FragmentBillShareViewPagerBinding
import com.shashankbhat.splitbill.ui.bill_shares.balance.ShowBillSharesBottomSheetFragment
import com.shashankbhat.splitbill.ui.bill_shares.shares.BillShareFragment
import com.shashankbhat.splitbill.util.ViewPagerAdapter
import com.shashankbhat.splitbill.viewmodels.BillShareViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillShareViewPager : Fragment() {

    private lateinit var binding: FragmentBillShareViewPagerBinding
    private val viewModel: BillShareViewModel by activityViewModels()
    private lateinit var groupListDto: GroupListDto

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillShareViewPagerBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupListDto = requireArguments().getSerializable("model") as GroupListDto

        val billSharesFragment = BillShareFragment.getInstance(requireArguments())
        val balanceFragment = ShowBillSharesBottomSheetFragment.getInstance()

        val adapterFragments = arrayListOf<Fragment>()
        adapterFragments.add(billSharesFragment)
        adapterFragments.add(balanceFragment)

        binding.vpBillShares.adapter = ViewPagerAdapter(requireActivity(), adapterFragments)

        binding.vpBillShares.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> binding.bottomNavigation.selectedItemId = R.id.menu_bill_shares
                    else -> {
                        binding.bottomNavigation.selectedItemId = R.id.menu_balances

                        balanceFragment.setBill(viewModel.billList.value?.data ?: emptyList())
                    }
                }
            }
        })

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_bill_shares -> binding.vpBillShares.setCurrentItem(0, true)
                R.id.menu_balances -> {
                    balanceFragment.setBill(viewModel.billList.value?.data ?: emptyList())
                    binding.vpBillShares.setCurrentItem(1, true)
                }
            }

            return@setOnItemSelectedListener true
        }

    }

}