package com.shashankbhat.splitbill.ui.bill_shares.shares

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shahankbhat.recyclergenericadapter.util.CallBackModel
import com.shashankbhat.splitbill.BR
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.base.BaseFragment
import com.shashankbhat.splitbill.database.local.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.databinding.AdapterBillShareBinding
import com.shashankbhat.splitbill.databinding.FragmentBillShareBinding
import com.shashankbhat.splitbill.enums.SnackBarType
import com.shashankbhat.splitbill.ui.bill_shares.add_bill.AddBillSharesBottomSheetFragment
import com.shashankbhat.splitbill.util.extension.showSnackBar
import com.shashankbhat.splitbill.viewmodels.BillShareViewModel

class BillShareFragment : BaseFragment() {

    private val viewModel: BillShareViewModel by activityViewModels()
    private lateinit var groupListDto: GroupListDto
    private lateinit var binding: FragmentBillShareBinding
    lateinit var adapter: RecyclerGenericAdapter<AdapterBillShareBinding, BillModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillShareBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

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
                add(CallBackModel(R.id.iv_more_option) { model, _, _ ->
                    binding.showSnackBar("Work in progress", "Okay", snackBarType = SnackBarType.INSTRUCTION)
                })
            })
            .build()
        (binding.rvNearbyUsers.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvNearbyUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNearbyUsers.adapter = adapter

        viewModel.billList.observe(viewLifecycleOwner) {
            if(it.isSuccess() || (it.isLoading() || (it.data?.size ?: 0) > 0)) {
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
            }
        }
    }

    private fun showAddBillBottomSheet(){
        val addBillDialog = AddBillSharesBottomSheetFragment(
            groupListDto = groupListDto,
            viewModel = viewModel
        )
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



