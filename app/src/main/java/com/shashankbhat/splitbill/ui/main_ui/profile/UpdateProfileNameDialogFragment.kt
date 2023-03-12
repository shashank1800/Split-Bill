package com.shashankbhat.splitbill.ui.main_ui.profile

import android.os.Bundle
import android.view.View
import com.shashankbhat.splitbill.base.BaseBottomSheetDialogFragment
import com.shashankbhat.splitbill.databinding.FragmentUpdateProfileNameDialogBinding

class UpdateProfileNameDialogFragment(
    val name: String?,
    val onUpdate: (String) -> Unit,
) : BaseBottomSheetDialogFragment<FragmentUpdateProfileNameDialogBinding>() {

    override fun getViewBinding() = FragmentUpdateProfileNameDialogBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tieName.setText(name)

        binding.btnUpdate.setOnClickListener {
            if(binding.tieName.text.toString().isEmpty()){
                binding.tilProfileName.isErrorEnabled = true
                return@setOnClickListener
            }
            onUpdate(binding.tieName.text.toString())
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(
            name: String?,
            onCreate: (String) -> Unit
        ) = UpdateProfileNameDialogFragment(name, onCreate)
    }
}