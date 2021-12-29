package com.example.splitbill.repository.local

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.splitbill.model.BillListDto
import com.example.splitbill.room_db.dao.BillShareDao
import com.example.splitbill.room_db.entity.BillShare

class BillShareRepository(private val billShareDao: BillShareDao) {

    suspend fun insert(billShare: BillShare) {
        billShareDao.insert(billShare)
    }

    suspend fun getAllBills(groupId: Int): List<BillListDto>? {
        return billShareDao.getAllBillShares(
            SimpleSQLiteQuery(
                "SELECT  b.id as bill_id, b.name as bill_name, b.total_amount, bs.user_id, " +
                        "u.name as user_name, bs.spent, bs.share, bs.id as bill_share_id from bill b " +
                        "INNER JOIN bill_share bs ON bs.bill_id = b.id " +
                        "INNER JOIN user u ON u.id = bs.user_id " +
                        "WHERE b.group_id = $groupId"
            )
        )
    }

}