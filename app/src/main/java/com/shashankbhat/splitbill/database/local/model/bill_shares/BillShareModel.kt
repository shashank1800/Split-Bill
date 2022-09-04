package com.shashankbhat.splitbill.database.local.model.bill_shares

import androidx.databinding.ObservableField
import com.shashankbhat.splitbill.database.local.dto.users.UserDto

data class BillShareModel(
    var user: UserDto,
    var spent : ObservableField<String> = ObservableField("0"),
    var share: ObservableField<String> = ObservableField("0")
)