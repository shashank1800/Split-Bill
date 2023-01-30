package com.shashankbhat.splitbill.database.remote.entity

import kotlinx.serialization.Serializable


@Serializable
data class GroupSaveDto(
    var name: String? = null,
    var peoples: List<Int>? = null
)