package com.shashankbhat.splitbill.repository.remote.entity

import com.shashankbhat.splitbill.room_db.entity.Bill
import com.shashankbhat.splitbill.room_db.entity.BillShare
import kotlinx.serialization.Serializable

@Serializable
data class BillSaveDto (
    var bill: Bill? = null,
    var billShares: List<BillShare>? = null
)
