package com.shashankbhat.splitbill.model

import com.shashankbhat.splitbill.util.bottom_sheet.BottomSheetHelper
import com.shashankbhat.splitbill.util.bottom_sheet.BottomSheetItem

data class ProfileIconModel(val id: Int, var url: String):
    BottomSheetHelper<ProfileIconModel> {
    override fun convertToBottomSheetItem(): BottomSheetItem<ProfileIconModel> {
        return BottomSheetItem(id, url, this)
    }
    override fun <T> getOriginalItem(): T {
        return this.getOriginalItem()
    }
}