package com.shashankbhat.splitbill.database.local.repository

import com.shashankbhat.splitbill.database.local.dao.BillDao
import com.shashankbhat.splitbill.database.local.entity.Bill

class BillRepository(private val billDao: BillDao) {

    suspend fun insert(bill: Bill?) {
        billDao.insert(bill)
    }

    suspend fun update(localId: Int, remoteId: Int) {
        billDao.update(localId, remoteId)
    }

    suspend fun getAllBill(groupId: Int): List<Bill> {
        return billDao.getAllBill(groupId)
    }

    suspend fun delete(bill: Bill): Int {
        return billDao.delete(bill)
    }

}