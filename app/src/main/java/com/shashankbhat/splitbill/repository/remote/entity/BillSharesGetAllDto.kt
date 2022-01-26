package com.shashankbhat.splitbill.repository.remote.entity

import com.shashankbhat.splitbill.dto.bill_shares.BillModel
import kotlinx.serialization.Serializable

@Serializable
class BillSharesGetAllDto (
    var data: List<BillModel>
)