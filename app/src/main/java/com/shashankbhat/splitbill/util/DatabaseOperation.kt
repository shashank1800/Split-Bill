package com.shashankbhat.splitbill.util

sealed class DatabaseOperation {
    object LOCAL : DatabaseOperation()
    object REMOTE : DatabaseOperation()

    fun isLocal(): Boolean = this is LOCAL
    fun isRemote(): Boolean = this is REMOTE
}