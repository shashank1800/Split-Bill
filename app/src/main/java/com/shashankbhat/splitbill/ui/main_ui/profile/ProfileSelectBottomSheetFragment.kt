package com.shashankbhat.splitbill.ui.main_ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shashankbhat.splitbill.base.BaseBottomSheetDialogFragment
import com.shashankbhat.splitbill.databinding.AdapterProfileIconBinding
import com.shashankbhat.splitbill.databinding.BottomSheetSingleItemSelectionBinding
import com.shashankbhat.splitbill.database.local.model.ProfileIconModel

class ProfileSelectBottomSheetFragment(
    val items: ArrayList<ProfileIconModel>,
    val onImageSelect : (ProfileIconModel) -> Unit
) : BaseBottomSheetDialogFragment<BottomSheetSingleItemSelectionBinding>() {

    override fun getViewBinding() = BottomSheetSingleItemSelectionBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.list.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.list.adapter = ItemAdapter()
    }

    private inner class ViewHolder(val itemBinding: AdapterProfileIconBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindTo(position: Int) {
            itemBinding.model = items[position]
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