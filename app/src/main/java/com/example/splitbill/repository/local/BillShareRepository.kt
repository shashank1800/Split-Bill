package com.example.splitbill.repository.local

import com.example.splitbill.room_db.dao.BillShareDao
import com.example.splitbill.room_db.entity.BillShare

class BillShareRepository(private val billShareDao: BillShareDao) {

    suspend fun insert(billShare: BillShare) {
        billShareDao.insert(billShare)
    }

}