package com.shashankbhat.splitbill.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.shashankbhat.splitbill.database.local.SplitBillDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesDatabase(@ApplicationContext context: Context): SplitBillDatabase {
        return SplitBillDatabase.invoke(context)
    }

    @Provides
    fun providesHttpClient(): HttpClient {
        return HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }
    }

    @Provides
    fun providesSharedPreference(@ApplicationContext context: Context): SharedPreferences {

        return context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

//        return EncryptedSharedPreferences.create(
//            context,
//            SHARED_PREFERENCE_NAME,
//            getMasterKey(context),
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
    }

//    private fun getMasterKey(context: Context): MasterKey {
//        return MasterKey.Builder(context)
//            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//            .build()
//    }
    const val SHARED_PREFERENCE_NAME = "com.splitbill"
}