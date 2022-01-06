package com.shashankbhat.splitbill.repository.local

import com.shashankbhat.splitbill.room_db.dao.BillDao
import com.shashankbhat.splitbill.room_db.entity.Bill

class BillRepository(private val billDao: BillDao) {

    suspend fun insert(bill: Bill): Long {
        return billDao.insert(bill)
    }

    suspend fun getAllBill(groupId: Int): List<Bill> {
        return billDao.getAllBill(groupId)
    }

}