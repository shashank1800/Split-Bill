package com.shashankbhat.splitbill.ui.user_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shashankbhat.splitbill.base.BaseBottomSheetDialogFragment
import com.shashankbhat.splitbill.databinding.FragmentAddGroupMembersBinding

class AddGroupMembersFragment(
    val onCreate: (String) -> Unit,
) : BaseBottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddGroupMembersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddGroupMembersBinding.inflate(inflater, container, false)
        return binding.root
    }

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