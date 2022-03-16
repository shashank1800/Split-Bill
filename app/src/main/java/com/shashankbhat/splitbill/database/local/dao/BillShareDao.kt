package com.shashankbhat.splitbill.database.local.dao

import androidx.room.*
import com.shashankbhat.splitbill.database.local.entity.BillShare

@Dao
interface BillShareDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(billShare: BillShare?)

    @Query("SELECT * FROM bill_share bs WHERE bill_id = :billId")
    suspend fun getBillShareByBillId(billId: Int): List<BillShare>

    @Delete
    suspend fun delete(billShare: BillShare?)
}