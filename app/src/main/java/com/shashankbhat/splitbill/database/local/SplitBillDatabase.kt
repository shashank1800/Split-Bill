package com.shashankbhat.splitbill.database.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shashankbhat.splitbill.database.local.dao.BillDao
import com.shashankbhat.splitbill.database.local.dao.BillShareDao
import com.shashankbhat.splitbill.database.local.entity.Groups
import com.shashankbhat.splitbill.database.local.entity.User
import com.shashankbhat.splitbill.database.local.dao.GroupDao
import com.shashankbhat.splitbill.database.local.dao.UserDao
import com.shashankbhat.splitbill.database.local.entity.Bill
import com.shashankbhat.splitbill.database.local.entity.BillShare
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
