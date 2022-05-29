package com.shashankbhat.splitbill.util.extension

import com.shashankbhat.splitbill.util.bottom_sheet.BottomSheetHelper
import com.shashankbhat.splitbill.util.bottom_sheet.BottomSheetItem


fun <T : BottomSheetHelper<T>> List<T>.getBottomSheetList(): ArrayList<BottomSheetItem<T>> {
    val list = arrayListOf<BottomSheetItem<T>>()
    this.forEach { list.add(it.convertToBottomSheetItem()) }
    return list
}