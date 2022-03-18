package com.shashankbhat.splitbill.database.local.dao

import androidx.room.*
import com.shashankbhat.splitbill.database.local.entity.BillShare

@Dao
interface BillShareDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(billShare: BillShare?)

    @Query("Update bill_share SET id = :billShareIdRemote, bill_id = :billIdRemote WHERE id = :billShareIdLocal AND bill_id = :billIdLocal ")
    suspend fun update(billIdLocal: Int, billShareIdLocal: Int, billIdRemote: Int, billShareIdRemote: Int)

    @Query("SELECT * FROM bill_share bs WHERE bill_id = :billId")
    suspend fun getBillShareByBillId(billId: Int): List<BillShare>

    @Delete
    suspend fun delete(billShare: BillShare?)
}