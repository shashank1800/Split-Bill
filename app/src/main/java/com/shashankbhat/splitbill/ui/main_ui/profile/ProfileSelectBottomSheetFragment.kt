package com.shashankbhat.splitbill.ui.main_ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.databinding.AdapterProfileIconBinding
import com.shashankbhat.splitbill.databinding.BottomSheetSingleItemSelectionBinding
import com.shashankbhat.splitbill.model.ProfileIconModel
import com.shashankbhat.splitbill.util.bottom_sheet.BottomSheetItem

class ProfileSelectBottomSheetFragment(
    val items: ArrayList<ProfileIconModel>,
    val onImageSelect : (ProfileIconModel) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetSingleItemSelectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetSingleItemSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.list.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.list.adapter = ItemAdapter()
    }

    private inner class ViewHolder(val itemBinding: AdapterProfileIconBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindTo(position: Int) {
            itemBinding.root.setOnClickListener{
                onImageSelect(items[position])
                dismiss()
            }
        }
    }

    private inner class ItemAdapter : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemBinding = AdapterProfileIconBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindTo(position)
        }

        override fun getItemCount(): Int = items.size
    }


}