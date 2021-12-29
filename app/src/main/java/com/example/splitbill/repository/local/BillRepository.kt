package com.example.splitbill.repository.local

import com.example.splitbill.room_db.dao.BillDao
import com.example.splitbill.room_db.entity.Bill

class BillRepository(private val billDao: BillDao) {

    suspend fun insert(bill: Bill): Long {
        return billDao.insert(bill)
    }

}