package com.shashankbhat.splitbill.model.profile

import com.shashankbhat.splitbill.util.bottom_sheet.BottomSheetHelper
import com.shashankbhat.splitbill.util.bottom_sheet.BottomSheetItem

data class DistanceRangeModel(var value: String, var distance: Double):
    BottomSheetHelper<DistanceRangeModel> {
    override fun convertToBottomSheetItem(): BottomSheetItem<DistanceRangeModel> {
        return BottomSheetItem(this.hashCode(), value, this)
    }
    override fun <T> getOriginalItem(): T {
        return this.getOriginalItem()
    }
}