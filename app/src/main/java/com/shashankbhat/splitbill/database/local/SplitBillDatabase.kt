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

        private const val DatabaseName = "articles_db.db"

        @Volatile
        private var instance: SplitBillDatabase? = null

//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE group_tbl ADD COLUMN date_created Long")
//            }
//        }

        // Check for DB instance if not null then get or insert or else create new DB Instance
        operator fun invoke(context: Context) = instance ?: synchronized(this) {
            instance ?: createDatabase(context)
                .also { instance = it }
        }

        // create db instance
        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            SplitBillDatabase::class.java,
            DatabaseName
        ).fallbackToDestructiveMigration().build()
    }
}
