package com.shashankbhat.splitbill.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class RecyclerItemOverlap(private val left: Int = 0, val top: Int = 0, private val right: Int = 0, val bottom : Int = 0) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == 0) {
            return
        }
        outRect.set(left, top, right, bottom)
    }
}