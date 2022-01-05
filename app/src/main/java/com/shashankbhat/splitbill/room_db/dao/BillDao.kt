package com.shashankbhat.splitbill.room_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.shashankbhat.splitbill.room_db.entity.Bill

@Dao
interface BillDao {
    @Insert
    suspend fun insert(bill: Bill?): Long

    @Query("SELECT * FROM bill WHERE bill.group_id = :groupId")
    suspend fun getAllBill(groupId: Int) : List<Bill>

}