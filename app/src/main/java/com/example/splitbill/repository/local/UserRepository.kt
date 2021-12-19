package com.example.splitbill.repository.local

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.splitbill.room_db.SplitBillDatabase
import com.example.splitbill.room_db.dao.UserDao
import com.example.splitbill.room_db.entity.User

class UserRepository(private val context: Context) {
    private var databaseInstance: SplitBillDatabase? = null
    private var userDao: UserDao? = null

    init {
        databaseInstance = SplitBillDatabase.getInstance(context)
        userDao = databaseInstance?.userDao()
    }

    suspend fun getAllUsersByGroupId(groupId: Int): LiveData<List<User>>? {
        return userDao?.getAllUserByGroupId(groupId)
    }

    suspend fun deleteUser(user: User) {
        userDao?.delete(user)
    }
}