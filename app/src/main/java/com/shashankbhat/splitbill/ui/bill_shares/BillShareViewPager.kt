package com.shashankbhat.splitbill.ui.bill_shares

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.base.BaseFragment
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.databinding.FragmentBillShareViewPagerBinding
import com.shashankbhat.splitbill.enums.SnackBarType
import com.shashankbhat.splitbill.util.ViewPagerAdapter
import com.shashankbhat.splitbill.util.extension.showSnackBar
import com.shashankbhat.splitbill.viewmodels.BillShareViewModel

class BillShareViewPager : BaseFragment() {

    private lateinit var binding: FragmentBillShareViewPagerBinding
    private lateinit var groupListDto: GroupListDto

    private val viewModel: BillShareViewModel by activityViewModels()
    private val adapterFragments = arrayListOf<Fragment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillShareViewPagerBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupListDto = requireArguments().getSerializable("model") as GroupListDto
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.isBillListEmpty = viewModel.isBillListEmpty

        val billSharesFragment = BillShareFragment.getInstance(requireArguments())
        val balanceFragment = ShowBillSharesBalanceFragment.getInstance(requireArguments())
        adapterFragments.add(billSharesFragment)
        adapterFragments.add(balanceFragment)

        binding.vpBillShares.adapter = ViewPagerAdapter(requireActivity(), adapterFragments)

//        binding.vpBillShares.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                when(position){
//                    0 -> binding.bottomNavigation.selectedItemId = R.id.menu_bill_shares
//                    else -> {
//                        binding.bottomNavigation.selectedItemId = R.id.menu_balances
//                    }
//                }
//            }
//        })

        binding.vpBillShares.isUserInputEnabled = false

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_bill_shares -> binding.vpBillShares.setCurrentItem(0, true)
                R.id.menu_balances -> {
                    binding.vpBillShares.setCurrentItem(1, true)
                }
                R.id.menu_statistics -> {
                    binding.showSnackBar("Work in progress", "Okay", snackBarType = SnackBarType.INSTRUCTION)
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