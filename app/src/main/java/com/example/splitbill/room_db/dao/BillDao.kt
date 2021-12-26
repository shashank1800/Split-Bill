package com.example.splitbill.room_db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.splitbill.model.BillListModel
import com.example.splitbill.room_db.entity.Bill

@Dao
interface BillDao {
    @Insert
    suspend fun insert(bill: Bill?): Long

    @RawQuery
    suspend fun getAllBills(query: SupportSQLiteQuery): List<BillListModel>?
}