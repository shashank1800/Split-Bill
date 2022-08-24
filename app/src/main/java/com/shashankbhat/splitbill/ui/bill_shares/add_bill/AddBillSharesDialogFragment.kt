package com.shashankbhat.splitbill.ui.bill_shares.add_bill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shashankbhat.splitbill.BR
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.base.BaseBottomSheetDialogFragment
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.database.local.model.bill_shares.BillShareModel
import com.shashankbhat.splitbill.databinding.AdapterAddBillSharesBinding
import com.shashankbhat.splitbill.databinding.FragmentAddBillSharesDialogBinding

class AddBillSharesDialogFragment(
    private val groupListDto: GroupListDto,
    val onCreate: (String, String, List<BillShareModel>) -> Unit,
) : BaseBottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddBillSharesDialogBinding
    private lateinit var adapter : RecyclerGenericAdapter<AdapterAddBillSharesBinding, BillShareModel>

    private val billShareInputList = arrayListOf<BillShareModel>().apply {
        groupListDto.userList?.forEach {
            add(BillShareModel(it))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBillSharesDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener {
            if (binding.tieName.text.toString().isEmpty()) {
                binding.tilGroupName.isErrorEnabled = true
                return@setOnClickListener
            }
            onCreate(binding.tieName.text.toString(), binding.tieTotalAmount.text.toString(), billShareInputList)
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.tieTotalAmount.doOnTextChanged { _, _, _, _ ->
            if(!binding.tieTotalAmount.text.isNullOrEmpty()){
                billShareInputList.forEach { billShareModel ->
                    val equallySharedFloat =
                        String.format("%.1f", (binding.tieTotalAmount.text.toString().toFloat() / billShareInputList.size))
                    val equallySharedInt =
                        String.format("%.0f", (binding.tieTotalAmount.text.toString().toFloat() / billShareInputList.size))
                    var result = equallySharedFloat
                    if (equallySharedFloat.toFloat() == equallySharedInt.toFloat()) // removes unwanted zeros at the end
                        result = equallySharedInt
                    billShareModel.share.set(result)
                }
            }
        }


        adapter = RecyclerGenericAdapter
            .Builder<AdapterAddBillSharesBinding, BillShareModel>(R.layout.adapter_add_bill_shares, BR.model)
            .build()
        binding.rvBillShares.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBillShares.adapter = adapter
        adapter.replaceList(billShareInputList)
    }

    companion object {

        @JvmStatic
        fun newInstance(
            groupListDto: GroupListDto,
            onCreate: (String, String, List<BillShareModel>) -> Unit
        ) = AddBillSharesDialogFragment(groupListDto, onCreate)
    }
}