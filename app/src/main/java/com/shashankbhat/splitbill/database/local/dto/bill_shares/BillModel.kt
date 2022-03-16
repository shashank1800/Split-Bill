package com.shashankbhat.splitbill.database.local.dto.bill_shares

import kotlinx.serialization.Serializable

@Serializable
data class BillModel(
    var id: Int? = null,
    var groupId: Int? = null,
    var name: String? = null,
    var totalAmount: Float? = null,
    var dateCreated: Long? = null,
    var billShares: List<BillSharesModel>? = null
)