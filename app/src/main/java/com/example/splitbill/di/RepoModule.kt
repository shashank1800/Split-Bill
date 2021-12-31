package com.example.splitbill.di

import com.example.splitbill.repository.local.BillRepository
import com.example.splitbill.repository.local.BillShareRepository
import com.example.splitbill.repository.local.GroupRepository
import com.example.splitbill.repository.local.UserRepository
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
object RepoModule {

    @Provides
    fun providesGroupRepo(groupDao: GroupDao): GroupRepository {
        return GroupRepository(groupDao)
    }

    @Provides
    fun providesUserRepo(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }

    @Provides
    fun providesBillRepo(billDao: BillDao): BillRepository {
        return BillRepository(billDao)
    }

    @Provides
    fun providesBillShareRepo(billShareDao: BillShareDao): BillShareRepository {
        return BillShareRepository(billShareDao)
    }

}