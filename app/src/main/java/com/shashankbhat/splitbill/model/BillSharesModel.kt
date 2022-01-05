package com.shashankbhat.splitbill.model

import com.shashankbhat.splitbill.room_db.entity.User

data class BillSharesModel(
    var id : Int?,
    var bill_id: Int?,
    var user_id: Int?,
    var spent: Float?,
    var share: Float?,
    var date_created: Long?,
    var user : User?
)