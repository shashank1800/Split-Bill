package com.shashankbhat.splitbill.ui.bill_shares

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shahankbhat.recyclergenericadapter.util.CallBackModel
import com.shahankbhat.recyclergenericadapter.util.DataBinds
import com.shahankbhat.recyclergenericadapter.util.MoreDataBindings
import com.shashankbhat.splitbill.BR
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.base.BaseFragment
import com.shashankbhat.splitbill.database.local.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.database.local.entity.Bill
import com.shashankbhat.splitbill.databinding.AdapterBillShareBinding
import com.shashankbhat.splitbill.databinding.FragmentBillShareBinding
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.alogrithm.BillSplitAlgorithm
import com.shashankbhat.splitbill.viewmodels.BillShareViewModel

class BillShareFragment : BaseFragment<FragmentBillShareBinding>() {

    private val viewModel: BillShareViewModel by activityViewModels()
    private lateinit var groupListDto: GroupListDto
    lateinit var adapter: RecyclerGenericAdapter<AdapterBillShareBinding, BillModel>

    override fun getViewBinding() = FragmentBillShareBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupListDto = requireArguments().getSerializable("model") as GroupListDto
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle(groupListDto.group?.name ?: "")
        binding.isBillListEmpty = viewModel.isBillListEmpty

        viewModel.getAllBill(groupListDto.group?.id ?: -1)

        binding.fab.setOnClickListener {
            showAddBillBottomSheet()
        }

        adapter = RecyclerGenericAdapter.Builder<AdapterBillShareBinding, BillModel>(R.layout.adapter_bill_share, BR.model)
            .setClickCallbacks(arrayListOf<CallBackModel<AdapterBillShareBinding, BillModel>>().apply {
                add(CallBackModel(R.id.iv_delete_option) { model, _, _ ->
                    val deleteDialog = MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete ${model.name}")
                        .setPositiveButton("Delete") { _, _ ->
                            viewModel.deleteBill(model)
                        }.setNegativeButton("Cancel", null)
                    deleteDialog.show()
                })
            })
            .setMoreDataBinds(DataBinds().apply {
                add(MoreDataBindings(BR.sharedPref, viewModel.sharedPreferences))
            })
            .build()
        (binding.rvNearbyUsers.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvNearbyUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNearbyUsers.adapter = adapter

        viewModel.billList.observe(viewLifecycleOwner) {
            if(it.isSuccess()) {
                if(adapter.getItemList().size == it.data?.size){
                    val oldList = adapter.getItemList()
                    val newList = it.data
                    val listSize = it.data.size
                    for(index in 0 until listSize){
                       if(newList[index] != oldList[index]){
                            // Replace complete group
                            adapter.replaceItemAt(index, newList[index])
                        }
                    }
                }else
                    adapter.replaceList(ArrayList(it.data ?: emptyList()))

                viewModel.isBillListEmpty.set((it?.data?.size ?: 0) == 0)
                viewModel.billListBalance.postValue(Response.success(it?.data))
                viewModel.billSplitAlgorithm = BillSplitAlgorithm(it?.data ?: emptyList())
                viewModel.isBalanceTransactionEmpty.set(
                    (viewModel.billSplitAlgorithm.getBalances()?.size ?: 0) > 0
                )
            }
        }
    }

    private fun showAddBillBottomSheet(){
        val addBillDialog = AddBillSharesDialogFragment.newInstance(
            groupListDto = groupListDto
        ) { name, total, billShareInputList ->
            var sumSpent = 0F
            var sumShare = 0F

            billShareInputList.forEach {
                sumSpent += it.spent.get()?.toFloat() ?: 0F
                sumShare += it.share.get()?.toFloat() ?: 0F
            }

            viewModel.addBill(
                Bill(
                    groupListDto.group?.id ?: -1,
                    name,
                    total.toFloat()
                ),
                billShareInputList
            )
        }
        addBillDialog.show(parentFragmentManager, addBillDialog.tag)
    }

    companion object {
        fun getInstance(bundle: Bundle): BillShareFragment {
            val fragment = BillShareFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}



