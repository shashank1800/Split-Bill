package com.shashankbhat.splitbill.util.bottom_sheet

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.databinding.BottomSheetItemBinding
import com.shashankbhat.splitbill.databinding.BottomSheetSingleItemSelectionBinding


class SingleItemSelectionBottomSheet<T : BottomSheetHelper<T>>(
    val items: ArrayList<BottomSheetItem<T>>,
    val eventListeners: EventListeners<T>,
    val selectedItem: ObservableField<T>? = null,
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetSingleItemSelectionBinding

    interface EventListeners<T> {
        fun onClick(position: Int, item: BottomSheetItem<T>)
    }

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
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = ItemAdapter()
    }

    private inner class ViewHolder(val itemBinding: BottomSheetItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindTo(position: Int) {
            itemBinding.itemName.text = items[position].value
            if(selectedItem?.get()?.convertToBottomSheetItem()?.id == items[position].id)
                TextViewCompat.setTextAppearance(itemBinding.itemName, R.style.BottomSheetItemTextSelected)
            else
                TextViewCompat.setTextAppearance(itemBinding.itemName, R.style.BottomSheetItemTextNormal)

            itemBinding.root.setOnClickListener {
                eventListeners.onClick(position, items[position])
                dialog?.dismiss()
            }

            //Ripple effect

            val pressedStates = ColorStateList.valueOf(ContextCompat.getColor(itemBinding.root.context, R.color.primaryColor))

            val contentDrawable = GradientDrawable()
            contentDrawable.setColor(Color.WHITE)
            contentDrawable.cornerRadius = 16f

            val rippleDrawable = RippleDrawable(pressedStates, contentDrawable, null)
            itemBinding.root.background = rippleDrawable

        }
    }

    private inner class ItemAdapter : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemBinding = BottomSheetItemBinding.inflate(
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