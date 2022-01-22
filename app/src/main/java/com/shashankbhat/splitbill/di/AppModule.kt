package com.shashankbhat.splitbill.di

import android.content.Context
import androidx.room.Room
import com.shashankbhat.splitbill.room_db.SplitBillDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.http.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesDatabase(@ApplicationContext context: Context): SplitBillDatabase {
        return Room.databaseBuilder(
            context,
            SplitBillDatabase::class.java,
            "split_bill_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providesHttpClient(): HttpClient {
        return HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }
        }
    }
}