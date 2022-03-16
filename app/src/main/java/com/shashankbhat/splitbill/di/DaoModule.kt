package com.shashankbhat.splitbill.di

import com.shashankbhat.splitbill.database.local.SplitBillDatabase
import com.shashankbhat.splitbill.database.local.dao.BillDao
import com.shashankbhat.splitbill.database.local.dao.BillShareDao
import com.shashankbhat.splitbill.database.local.dao.GroupDao
import com.shashankbhat.splitbill.database.local.dao.UserDao
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