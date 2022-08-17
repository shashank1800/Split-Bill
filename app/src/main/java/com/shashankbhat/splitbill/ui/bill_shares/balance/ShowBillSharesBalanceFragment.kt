package com.shashankbhat.splitbill.ui.bill_shares.balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shashankbhat.splitbill.BR
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.util.alogrithm.BillSplitAlgorithm
import com.shashankbhat.splitbill.base.TitleFragment
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.databinding.*
import com.shashankbhat.splitbill.util.alogrithm.BillShareBalance
import com.shashankbhat.splitbill.util.alogrithm.BillSpentAndShare
import com.shashankbhat.splitbill.viewmodels.BillShareViewModel

class ShowBillSharesBalanceFragment : TitleFragment() {

    private lateinit var binding: FragmentBillSharesBalancesBinding
    private lateinit var groupListDto: GroupListDto

    private lateinit var adapter: RecyclerGenericAdapter<AdapterBillShareBalancesBinding, BillShareBalance>
    private lateinit var adapterTotal: RecyclerGenericAdapter<AdapterBillSharesTotalBinding, BillSpentAndShare>

    private val viewModel: BillShareViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillSharesBalancesBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupListDto = requireArguments().getSerializable("model") as GroupListDto
        binding.isBillListEmpty = viewModel.isBillListEmpty

        initBalanceRecyclerView()
        initTotalRecyclerView()

        if((viewModel.billListBalance.value?.data?.size ?: 0) > 0) {
            adapter.replaceList(ArrayList(viewModel.billSplitAlgorithm.getBalances() ?: emptyList()))
            adapterTotal.replaceList(ArrayList(viewModel.billSplitAlgorithm.getSharesAndBalance() ?: emptyList()))
        }

        viewModel.billListBalance.observe(viewLifecycleOwner) {
            if(it.isSuccess() && (it.data?.size ?: 0) > 0) {
                viewModel.billSplitAlgorithm = BillSplitAlgorithm(it.data ?: emptyList())
                adapter.replaceList(ArrayList(viewModel.billSplitAlgorithm.getBalances() ?: emptyList()))
                adapterTotal.replaceList(ArrayList(viewModel.billSplitAlgorithm.getSharesAndBalance() ?: emptyList()))
            }
        }
    }

    private fun initBalanceRecyclerView(){
        adapter = RecyclerGenericAdapter.Builder<AdapterBillShareBalancesBinding, BillShareBalance>(R.layout.adapter_bill_share_balances, BR.model)
            .build()
        (binding.rvList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = adapter
    }

    private fun initTotalRecyclerView(){
        adapterTotal = RecyclerGenericAdapter.Builder<AdapterBillSharesTotalBinding, BillSpentAndShare>(R.layout.adapter_bill_shares_total, BR.model)
            .build()
        (binding.rvListTotal.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvListTotal.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListTotal.adapter = adapterTotal
    }

    companion object {
        fun getInstance(bundle: Bundle): ShowBillSharesBalanceFragment {
            val fragment = ShowBillSharesBalanceFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}
