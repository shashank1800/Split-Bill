package com.shashankbhat.splitbill.ui.bill_shares.balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shashankbhat.splitbill.BR
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.util.alogrithm.BillSplitAlgorithm
import com.shashankbhat.splitbill.base.TitleFragment
import com.shashankbhat.splitbill.database.local.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.databinding.*
import com.shashankbhat.splitbill.ui.bill_shares.shares.BillShareFragment
import com.shashankbhat.splitbill.util.alogrithm.BillShareBalance
import com.shashankbhat.splitbill.util.alogrithm.BillSpentAndShare
import com.shashankbhat.splitbill.viewmodels.BillShareViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowBillSharesBottomSheetFragment : TitleFragment() {

    private var billSplitAlgorithm: BillSplitAlgorithm? = null

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

        viewModel.getAllBillForShowingBalances(groupListDto.group.id ?: -1)

        viewModel.billListBalance.observe(viewLifecycleOwner) {
            if(it.isSuccess() && (it.data?.size ?: 0) > 0) {
                billSplitAlgorithm = BillSplitAlgorithm(it.data ?: emptyList())
                adapter.replaceList(ArrayList(billSplitAlgorithm?.getBalances() ?: emptyList()))
                adapterTotal.replaceList(ArrayList(billSplitAlgorithm?.getSharesAndBalance() ?: emptyList()))

                viewModel.isBillListEmpty.set(it.data?.size == 0)
            }
        }
    }

    private fun initBalanceRecyclerView(){
        adapter = RecyclerGenericAdapter.Builder<AdapterBillShareBalancesBinding, BillShareBalance>(R.layout.adapter_bill_share_balances, BR.model)
            .build()

        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = adapter
    }

    private fun initTotalRecyclerView(){
        adapterTotal = RecyclerGenericAdapter.Builder<AdapterBillSharesTotalBinding, BillSpentAndShare>(R.layout.adapter_bill_shares_total, BR.model)
            .build()

        binding.rvListTotal.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListTotal.adapter = adapterTotal
    }


//    @Composable
//    fun ShowBillShares() {
//
//        Column(
//            modifier = Modifier
//                .padding(8.dp)
//        ) {
//
//            if(billSplitAlgorithm.value.getBalances()?.size != 0){
//                Text(
//                    text = "Balance",
//                    style = Typography.h6
//                )
//
//                LazyColumn {
//                    items(billSplitAlgorithm.value.getBalances() ?: emptyList()) { billShare ->
//                        BalanceCard(billShare)
//                    }
//                }
//
//            }
//
//            Text(
//                text = "Total",
//                modifier = Modifier.padding(top = 8.dp),
//                style = Typography.h6
//            )
//
//            SpentAndShareHead()
//
//            LazyColumn {
//                items(billSplitAlgorithm.value.getSharesAndBalance()) { shareAndBalance ->
//                    SpentAndShareCard(shareAndBalance)
//                }
//            }
//
//
//        }
//
//    }

//    @Composable
//    fun SpentAndShareHead() {
//        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
//            val (tvUserName, tvSpentAmount, tvShareAmount, tvBalance) = createRefs()
//
//            Text(
//                text = "Name",
//                modifier = Modifier.constrainAs(tvUserName) {
//                    top.linkTo(parent.top)
//                    start.linkTo(parent.start)
//                    end.linkTo(tvSpentAmount.start, margin = 16.dp)
//                    width = Dimension.fillToConstraints
//                },
//                style = Typography.h1
//            )
//
//            Text(
//                text = "Spent",
//                modifier = Modifier.constrainAs(tvSpentAmount) {
//                    top.linkTo(parent.top)
//                    start.linkTo(tvUserName.end)
//                    end.linkTo(tvShareAmount.start, margin = 16.dp)
//                    width = Dimension.fillToConstraints
//                },
//                style = Typography.h1
//            )
//
//            Text(
//                text = "Share",
//                modifier = Modifier.constrainAs(tvShareAmount) {
//                    top.linkTo(parent.top)
//                    start.linkTo(tvSpentAmount.end)
//                    end.linkTo(tvBalance.end)
//                    width = Dimension.fillToConstraints
//                },
//                style = Typography.h1
//            )
//
//            Text(
//                text = "Balance",
//                modifier = Modifier.constrainAs(tvBalance) {
//                    top.linkTo(parent.top)
//                    start.linkTo(tvShareAmount.end)
//                    end.linkTo(parent.end)
//                    width = Dimension.fillToConstraints
//                },
//                style = Typography.h1
//            )
//        }
//    }

    companion object {
        fun getInstance(bundle: Bundle): ShowBillSharesBottomSheetFragment {
            val fragment = ShowBillSharesBottomSheetFragment()
            fragment.arguments = bundle
            return fragment
            return fragment
        }
    }

//    @Preview(showBackground = true)
//    @Composable
//    fun ShowBillSharesPreview() {
//        SplitBillTheme {
//            ShowBillShares()
//        }
//    }


}
