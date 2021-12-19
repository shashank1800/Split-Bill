package com.example.splitbill.repository.local

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.splitbill.model.GroupListModel
import com.example.splitbill.room_db.entity.Group
import com.example.splitbill.room_db.entity.User
import com.example.splitbill.room_db.dao.GroupDao
import com.example.splitbill.room_db.SplitBillDatabase
import com.example.splitbill.room_db.dao.UserDao

class GroupRepository(private val context: Context) {
    private var databaseInstance: SplitBillDatabase? = null
    private var groupDao: GroupDao? = null
    private var userDao : UserDao? = null

    init {
        databaseInstance = SplitBillDatabase.getInstance(context)
        groupDao = databaseInstance?.groupDao()
        userDao = databaseInstance?.userDao()
    }

    suspend fun insert(group: Group) {
        groupDao?.insert(group)
    }

    suspend fun getAllGroups(): LiveData<List<GroupListModel>>? {
        return groupDao?.getAllGroups()
    }

    suspend fun insertUser(user: User) {
        userDao?.insert(user)
    }
}