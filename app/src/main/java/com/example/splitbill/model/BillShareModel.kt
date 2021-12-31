package com.example.splitbill.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class BillShareModel(
    var user_id: Int,
    var spent : MutableState<String> = mutableStateOf("0"),
    var share: MutableState<String> = mutableStateOf("0")
)