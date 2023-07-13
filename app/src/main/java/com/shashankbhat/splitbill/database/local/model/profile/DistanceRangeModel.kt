package com.shashankbhat.splitbill.database.local.model.profile

import com.shashankbhat.splitbill.util.bottom_sheet.BottomSheetHelper
import com.shashankbhat.splitbill.util.bottom_sheet.BottomSheetItem

data class DistanceRangeModel(var value: String, var distance: Double):
    BottomSheetHelper<DistanceRangeModel> {
    override fun convertToBottomSheetItem(): BottomSheetItem<DistanceRangeModel> {
        return BottomSheetItem(distance.toInt(), value, this)
    }
    override fun <T> getOriginalItem(): T {
        return this.getOriginalItem()
    }
}