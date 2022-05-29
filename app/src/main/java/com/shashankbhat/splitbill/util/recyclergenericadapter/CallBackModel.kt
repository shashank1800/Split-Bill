package com.shashankbhat.splitbill.util.recyclergenericadapter

import androidx.databinding.ViewDataBinding

/**
 * Created by SHASHANK BHAT on 29-May-22.
 *
 *
 */

class CallBackModel<BIND_TYPE : ViewDataBinding, MODEL_TYPE>(
    val id: Int,
    val block: (model: MODEL_TYPE, position: Int, binding: BIND_TYPE) -> Unit
)