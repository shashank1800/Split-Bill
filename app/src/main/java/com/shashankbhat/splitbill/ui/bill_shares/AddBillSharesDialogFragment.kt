package com.shashankbhat.splitbill.ui.bill_shares

import android.os.Bundle
import android.view.View
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
import com.shashankbhat.splitbill.enums.SnackBarType
import com.shashankbhat.splitbill.util.extension.showSnackBar

class AddBillSharesDialogFragment(
    private val groupListDto: GroupListDto,
    val onCreate: (String, String, List<BillShareModel>) -> Unit,
) : BaseBottomSheetDialogFragment<FragmentAddBillSharesDialogBinding>() {

    private lateinit var adapter : RecyclerGenericAdapter<AdapterAddBillSharesBinding, BillShareModel>

    override fun getViewBinding() = FragmentAddBillSharesDialogBinding.inflate(layoutInflater)

    private val billShareInputList = arrayListOf<BillShareModel>().apply {
        groupListDto.userList?.forEach {
            add(BillShareModel(it))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener {
            if (binding.tieName.text.toString().isEmpty()) {
                binding.tilGroupName.isErrorEnabled = true
                return@setOnClickListener
            }

            var sumShare = 0F
            var sumSpent = 0F
            billShareInputList.forEach { billShareModel ->
                sumShare += billShareModel.share.get()?.toFloat() ?: 0f
                sumSpent += billShareModel.spent.get()?.toFloat() ?: 0f
            }

            if (binding.tieTotalAmount.text.toString()
                    .toFloat() != sumShare || sumShare != sumSpent
            ) {
                binding.layoutInstruction.instructionView.visibility = View.VISIBLE
                return@setOnClickListener
            }

            onCreate(
                binding.tieName.text.toString(), binding.tieTotalAmount.text.toString(),
                billShareInputList
            )
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.tieTotalAmount.doOnTextChanged { _, _, _, _ ->
            if(!binding.tieTotalAmount.text.isNullOrEmpty()){
                var sumShare = 0F
                billShareInputList.forEach { billShareModel ->
                    val equallySharedFloat =
                        String.format("%.1f", (binding.tieTotalAmount.text.toString().toFloat() / billShareInputList.size))
                    val equallySharedInt =
                        String.format("%.0f", (binding.tieTotalAmount.text.toString().toFloat() / billShareInputList.size))
                    var result = equallySharedFloat
                    if (equallySharedFloat.toFloat() == equallySharedInt.toFloat()) // removes unwanted zeros at the end
                        result = equallySharedInt
                    billShareModel.share.set(result)
                    sumShare += result.toFloat()
                }
                if (billShareInputList.size > 0){
                    val equallySharedFloat = String.format(
                        "%.1f", (billShareInputList[0].share.get()?.toFloat() ?: 0F)
                                + (binding.tieTotalAmount.text.toString().toFloat() - sumShare)
                    )

                    val equallySharedInt =
                        String.format(
                            "%.0f", (billShareInputList[0].share.get()?.toFloat() ?: 0F)
                                    + (binding.tieTotalAmount.text.toString().toFloat() - sumShare)
                        )
                    var result = equallySharedFloat
                    if (equallySharedFloat.toFloat() == equallySharedInt.toFloat()) // removes unwanted zeros at the end
                        result = equallySharedInt
                    billShareInputList[0].share.set(
                        result
                    )
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