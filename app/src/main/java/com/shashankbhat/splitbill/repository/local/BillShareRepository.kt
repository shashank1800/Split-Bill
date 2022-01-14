package com.shashankbhat.splitbill.repository.local

import com.shashankbhat.splitbill.room_db.dao.BillShareDao
import com.shashankbhat.splitbill.room_db.entity.BillShare

class BillShareRepository(private val billShareDao: BillShareDao) {

    suspend fun insert(billShare: BillShare) {
        billShareDao.insert(billShare)
    }

    suspend fun getBillShareByBillId(billId: Int): List<BillShare> {
        return billShareDao.getBillShareByBillId(billId)
    }

    suspend fun delete(billShare: BillShare) {
        billShareDao.delete(billShare)
    }
}