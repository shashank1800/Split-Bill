package com.shashankbhat.splitbill.di

import com.shashankbhat.splitbill.database.local.repository.BillRepository
import com.shashankbhat.splitbill.database.local.repository.BillShareRepository
import com.shashankbhat.splitbill.database.local.repository.GroupRepository
import com.shashankbhat.splitbill.database.local.repository.UserRepository
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