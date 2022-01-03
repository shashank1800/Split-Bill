package com.shashankbhat.splitbill.room_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.shashankbhat.splitbill.model.BillListDto
import com.shashankbhat.splitbill.room_db.entity.BillShare

@Dao
interface BillShareDao {

    @Insert
    suspend fun insert(billShare: BillShare?)

    @RawQuery
    suspend fun getAllBillShares(query: SupportSQLiteQuery): List<BillListDto>?
}