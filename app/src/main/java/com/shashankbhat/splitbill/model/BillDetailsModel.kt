package com.shashankbhat.splitbill.model

data class BillDetailsModel(
    var bill_id : Int,
    var bill_name: String,
    var total_amount: Float,
    var user_id: Int,
    var user_name:String,
    var spent: Float,
    var share: Float,
    var bill_share_id: Int,
    var date_created: Long
)