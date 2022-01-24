package com.shashankbhat.splitbill.room_db.dao

import androidx.room.*
import com.shashankbhat.splitbill.room_db.entity.Bill

@Dao
interface BillDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bill: Bill?)

    @Query("SELECT * FROM bill WHERE bill.group_id = :groupId")
    suspend fun getAllBill(groupId: Int) : List<Bill>

    @Delete
    suspend fun delete(bill: Bill?): Int
}