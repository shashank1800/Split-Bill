package com.shashankbhat.splitbill.ui.user_list

import android.os.Bundle
import android.view.View
import com.shashankbhat.splitbill.base.BaseBottomSheetDialogFragment
import com.shashankbhat.splitbill.databinding.FragmentAddGroupMembersBinding

class AddGroupMembersFragment(
    val onCreate: (String) -> Unit,
) : BaseBottomSheetDialogFragment<FragmentAddGroupMembersBinding>() {

    override fun getViewBinding() = FragmentAddGroupMembersBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener {
            if(binding.tieName.text.toString().isEmpty()){
                binding.tilGroupName.isErrorEnabled = true
                return@setOnClickListener
            }
            onCreate(binding.tieName.text.toString())
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(
            onCreate: (String) -> Unit
        ) = AddGroupMembersFragment(onCreate)
    }
}