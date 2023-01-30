package com.shashankbhat.splitbill.database.local.dto.group_list

import java.io.Serializable


@kotlinx.serialization.Serializable
data class GroupsDto(
    var name: String,
    var id: Int? = null,
    var dateCreated: Long? = null,
    var uniqueId: Int? = null
): Serializable