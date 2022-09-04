package com.shashankbhat.splitbill.base

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shashankbhat.splitbill.R

open class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
}