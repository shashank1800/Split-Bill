package com.shashankbhat.splitbill.database.local.dto.bill_shares

import com.shashankbhat.splitbill.database.local.entity.User
import kotlinx.serialization.Serializable

@Serializable
data class BillSharesModel(
    var id : Int?,
    var billId: Int?,
    var userId: Int?,
    var spent: Float?,
    var share: Float?,
    var dateCreated: Long?,
    var user : User?
)