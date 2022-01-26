package com.shashankbhat.splitbill.dto.bill_shares

import com.shashankbhat.splitbill.room_db.entity.User
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