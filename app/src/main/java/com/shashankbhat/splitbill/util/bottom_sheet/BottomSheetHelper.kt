package com.shashankbhat.splitbill.util.bottom_sheet

interface BottomSheetHelper<T> {
    fun convertToBottomSheetItem(): BottomSheetItem<T>
    fun <T> getOriginalItem(): T
}