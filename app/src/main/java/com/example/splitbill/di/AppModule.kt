package com.example.splitbill.di

import android.content.Context
import com.example.splitbill.repository.local.GroupRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesGroupRepo(@ApplicationContext context: Context): GroupRepository {
        return GroupRepository(context)
    }
}