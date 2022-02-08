package com.shashankbhat.splitbill.di

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
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

    @Provides
    fun providesSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        var sharedPreferences: SharedPreferences

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sharedPreferences = EncryptedSharedPreferences.create(
                context,
                SHARED_PREFERENCE_NAME,
                getMasterKey(context),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

        } else {
            sharedPreferences =
                context.getSharedPreferences(
                    SHARED_PREFERENCE_NAME,
                    Context.MODE_PRIVATE
                )
        }
        return sharedPreferences
    }

    private fun getMasterKey(context: Context): MasterKey {
        return MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }
    const val SHARED_PREFERENCE_NAME = "com.splitbill"
}