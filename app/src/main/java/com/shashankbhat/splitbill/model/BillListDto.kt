package com.shashankbhat.splitbill.model

import androidx.room.Embedded

data class BillListDto(
    @Embedded
    var billDetails: BillDetailsModel?,
)
