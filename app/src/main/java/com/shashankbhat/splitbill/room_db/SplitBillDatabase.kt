package com.shashankbhat.splitbill.room_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shashankbhat.splitbill.room_db.dao.BillDao
import com.shashankbhat.splitbill.room_db.dao.BillShareDao
import com.shashankbhat.splitbill.room_db.entity.Groups
import com.shashankbhat.splitbill.room_db.entity.User
import com.shashankbhat.splitbill.room_db.dao.GroupDao
import com.shashankbhat.splitbill.room_db.dao.UserDao
import com.shashankbhat.splitbill.room_db.entity.Bill
import com.shashankbhat.splitbill.room_db.entity.BillShare
import com.shashankbhat.splitbill.util.converters.DateConverter

@Database(entities = [Groups::class, User::class, Bill::class, BillShare::class], exportSchema = false, version = 2)
@TypeConverters(value = [DateConverter::class])
abstract class SplitBillDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun userDao(): UserDao
    abstract fun billDao(): BillDao
    abstract fun billShareDao(): BillShareDao

    companion object {
        var instance: SplitBillDatabase? = null

//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE group_tbl ADD COLUMN date_created Long")
//            }
//        }


        @Synchronized
        fun getInstance(context: Context): SplitBillDatabase? {
            if (instance == null)
                instance = Room
                    .databaseBuilder(
                        context,
                        SplitBillDatabase::class.java,
                        "split_bill_db"
                    ).fallbackToDestructiveMigration()
                    .build()
            return instance
        }
    }
}
