package com.shashankbhat.splitbill.database.remote.entity

import com.shashankbhat.splitbill.database.local.dto.bill_shares.BillModel
import kotlinx.serialization.Serializable

@Serializable
class BillSharesGetAllDto (
    var data: List<BillModel>
)