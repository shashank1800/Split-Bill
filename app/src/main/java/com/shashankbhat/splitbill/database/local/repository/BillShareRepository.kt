package com.shashankbhat.splitbill.database.local.repository

import com.shashankbhat.splitbill.database.local.dao.BillShareDao
import com.shashankbhat.splitbill.database.local.entity.BillShare

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