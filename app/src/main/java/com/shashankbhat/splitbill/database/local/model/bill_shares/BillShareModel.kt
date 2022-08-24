package com.shashankbhat.splitbill.database.local.model.bill_shares

import androidx.databinding.ObservableField
import com.shashankbhat.splitbill.database.local.entity.User

data class BillShareModel(
    var user: User,
    var spent : ObservableField<String> = ObservableField("0"),
    var share: ObservableField<String> = ObservableField("0")
)