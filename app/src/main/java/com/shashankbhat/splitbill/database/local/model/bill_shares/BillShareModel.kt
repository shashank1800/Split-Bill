package com.shashankbhat.splitbill.database.local.model.bill_shares

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class BillShareModel(
    var userId: Int,
    var spent : MutableState<String> = mutableStateOf("0"),
    var share: MutableState<String> = mutableStateOf("0")
)