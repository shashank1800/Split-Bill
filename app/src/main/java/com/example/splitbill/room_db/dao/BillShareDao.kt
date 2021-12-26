package com.example.splitbill.room_db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.splitbill.room_db.entity.BillShare

@Dao
interface BillShareDao {

    @Insert
    suspend fun insert(billShare: BillShare?)
}