package com.example.splitbill.room_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.splitbill.room_db.entity.Group
import com.example.splitbill.room_db.entity.User
import com.example.splitbill.room_db.dao.GroupDao
import com.example.splitbill.room_db.dao.UserDao
import com.example.splitbill.util.converters.DateConverter

@Database(entities = [Group::class, User::class], exportSchema = false, version = 1)
@TypeConverters(value = [DateConverter::class])
abstract class SplitBillDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun userDao(): UserDao

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
