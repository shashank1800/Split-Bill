package com.example.splitbill.di

import com.example.splitbill.room_db.SplitBillDatabase
import com.example.splitbill.room_db.dao.BillDao
import com.example.splitbill.room_db.dao.BillShareDao
import com.example.splitbill.room_db.dao.GroupDao
import com.example.splitbill.room_db.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    fun providesUserDao(databaseInstance: SplitBillDatabase): UserDao {
        return databaseInstance.userDao()
    }

    @Provides
    fun providesGroupDao(databaseInstance: SplitBillDatabase): GroupDao {
        return databaseInstance.groupDao()
    }

    @Provides
    fun providesBillDao(databaseInstance: SplitBillDatabase): BillDao {
        return databaseInstance.billDao()
    }

    @Provides
    fun providesBillShareDao(databaseInstance: SplitBillDatabase): BillShareDao {
        return databaseInstance.billShareDao()
    }

}