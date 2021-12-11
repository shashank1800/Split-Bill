package com.example.splitbill.repository.local

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.splitbill.model.Group
import com.example.splitbill.model.GroupDao
import com.example.splitbill.room_db.SplitBillDatabase
import kotlinx.coroutines.flow.MutableStateFlow

class GroupRepository(private val context: Context) {
    private var databaseInstance: SplitBillDatabase? = null
    private var groupDao: GroupDao? = null

    init {
        databaseInstance = SplitBillDatabase.getInstance(context)
        groupDao = databaseInstance?.groupDao()
    }

    suspend fun insert(group: Group) {
        groupDao?.insert(group)
    }

    suspend fun getAllGroups(): LiveData<List<Group>>? {
        return groupDao?.getAllGroups()
    }
}