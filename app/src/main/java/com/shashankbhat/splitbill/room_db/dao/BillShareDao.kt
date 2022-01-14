package com.shashankbhat.splitbill.room_db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.shashankbhat.splitbill.room_db.entity.BillShare

@Dao
interface BillShareDao {

    @Insert
    suspend fun insert(billShare: BillShare?)

    @Query("SELECT * FROM bill_share bs WHERE bill_id = :billId")
    suspend fun getBillShareByBillId(billId: Int): List<BillShare>

    @Delete
    suspend fun delete(billShare: BillShare?)
}