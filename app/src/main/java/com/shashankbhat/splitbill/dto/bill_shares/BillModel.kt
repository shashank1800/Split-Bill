package com.shashankbhat.splitbill.dto.bill_shares

data class BillModel(
    var id: Int? = null,
    var group_id: Int? = null,
    var name: String? = null,
    var total_amount: Float? = null,
    var date_created: Long? = null,
    var billShares: List<BillSharesModel>? = null
)