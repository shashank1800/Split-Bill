package com.example.splitbill.room_db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.splitbill.room_db.entity.Bill

@Dao
interface BillDao {
    @Insert
    suspend fun insert(bill: Bill?): Long

}