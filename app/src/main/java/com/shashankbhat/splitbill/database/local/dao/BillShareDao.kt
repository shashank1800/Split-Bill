package com.shashankbhat.splitbill.database.local.dao

import androidx.room.*
import com.shashankbhat.splitbill.database.local.entity.BillShare

@Dao
interface BillShareDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(billShare: BillShare?)

    @Query("Update bill_share SET id = :billShareIdRemote, bill_id = :billIdRemote WHERE id = :billShareIdLocal")
    suspend fun update(billShareIdLocal: Int, billIdRemote: Int, billShareIdRemote: Int)

    @Query("SELECT * FROM bill_share bs WHERE bill_id = :billId ORDER BY date_created DESC")
    suspend fun getBillShareByBillId(billId: Int): List<BillShare>

    @Delete
    suspend fun delete(billShare: BillShare?)
}