package com.shashankbhat.splitbill.database.local.model

import androidx.databinding.ObservableBoolean

data class NearUserModel(
    var uniqueId: Int,
    var name: String?,
    var photoUrl: String?,
    var latitude: Double?,
    var longitude: Double?
){
    var isSelected = ObservableBoolean(false)
}