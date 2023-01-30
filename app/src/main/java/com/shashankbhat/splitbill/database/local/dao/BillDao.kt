package com.shashankbhat.splitbill.database.local.dao

import androidx.room.*
import com.shashankbhat.splitbill.database.local.entity.Bill

@Dao
interface BillDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bill: Bill?)

    @Query("Update bill SET id = :remoteId WHERE id = :localId")
    suspend fun update(localId: Int, remoteId: Int)

    @Query("SELECT * FROM bill WHERE bill.group_id = :groupId ORDER BY date_created DESC")
    suspend fun getAllBill(groupId: Int) : List<Bill>

    @Delete
    suspend fun delete(bill: Bill?): Int
}