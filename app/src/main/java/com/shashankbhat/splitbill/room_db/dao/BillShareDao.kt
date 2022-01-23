package com.shashankbhat.splitbill.room_db.dao

import androidx.room.*
import com.shashankbhat.splitbill.room_db.entity.BillShare

@Dao
interface BillShareDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(billShare: BillShare?)

    @Query("SELECT * FROM bill_share bs WHERE bill_id = :billId")
    suspend fun getBillShareByBillId(billId: Int): List<BillShare>

    @Delete
    suspend fun delete(billShare: BillShare?)
}