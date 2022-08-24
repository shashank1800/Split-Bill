package com.shashankbhat.splitbill.ui.user_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shashankbhat.splitbill.base.BaseBottomSheetDialogFragment
import com.shashankbhat.splitbill.databinding.FragmentLinkGroupMemberDialogBinding

class LinkGroupMemberDialogFragment(
    val onCreate: (String) -> Unit,
) : BaseBottomSheetDialogFragment() {
    private lateinit var binding: FragmentLinkGroupMemberDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLinkGroupMemberDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

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