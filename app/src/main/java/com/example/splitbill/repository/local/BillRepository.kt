package com.example.splitbill.repository.local

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.splitbill.model.BillListModel
import com.example.splitbill.room_db.dao.BillDao
import com.example.splitbill.room_db.entity.Bill

class BillRepository(private val billDao: BillDao) {


    suspend fun insert(bill: Bill): Long {
        return billDao.insert(bill)
    }

    suspend fun getAllBills(groupId: Int): List<BillListModel>? {
        return billDao.getAllBills(SimpleSQLiteQuery("SELECT * from bill b WHERE b.group_id = $groupId"))
    }

}