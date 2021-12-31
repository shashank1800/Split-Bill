package com.example.splitbill.model

import androidx.room.Embedded

data class BillListDto(
    @Embedded
    var billDetails: BillDetailsModel?,
)
