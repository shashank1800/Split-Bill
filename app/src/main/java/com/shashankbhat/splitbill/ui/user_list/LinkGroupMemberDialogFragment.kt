package com.shashankbhat.splitbill.ui.user_list

import android.os.Bundle
import android.view.View
import com.shashankbhat.splitbill.base.BaseBottomSheetDialogFragment
import com.shashankbhat.splitbill.databinding.FragmentLinkGroupMemberDialogBinding

class LinkGroupMemberDialogFragment(
    val onCreate: (String) -> Unit,
) : BaseBottomSheetDialogFragment<FragmentLinkGroupMemberDialogBinding>() {

    override fun getViewBinding() = FragmentLinkGroupMemberDialogBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener {
            if(binding.tieUserId.text.toString().isEmpty()){
                binding.tilUserId.isErrorEnabled = true
                return@setOnClickListener
            }
            onCreate(binding.tieUserId.text.toString())
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
        ) = LinkGroupMemberDialogFragment(onCreate)
    }
}