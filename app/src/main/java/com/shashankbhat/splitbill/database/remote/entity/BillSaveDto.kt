package com.shashankbhat.splitbill.database.remote.entity

import com.shashankbhat.splitbill.database.local.entity.Bill
import com.shashankbhat.splitbill.database.local.entity.BillShare
import kotlinx.serialization.Serializable

@Serializable
data class BillSaveDto (
    var bill: Bill? = null,
    var billShares: List<BillShare>? = null
)
