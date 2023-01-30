package com.shashankbhat.splitbill.di

import android.content.Context
import android.content.SharedPreferences
import com.shashankbhat.splitbill.database.local.SplitBillDatabase
import com.shashankbhat.splitbill.util.KnownException
import com.shashankbhat.splitbill.util.KnownExceptionDto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.serialization.decodeFromString

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
            expectSuccess = true
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

            HttpResponseValidator {
                handleResponseException { exception ->
                    val clientException = exception as? ClientRequestException ?: return@handleResponseException
                    val exceptionResponse = clientException.response
                    if (exceptionResponse.status == HttpStatusCode.BadRequest) {
                        val exceptionResponseText = exceptionResponse.content
                        exceptionResponseText.readUTF8Line()?.let {
                            val jsonElement = kotlinx.serialization.json.Json.decodeFromString<KnownExceptionDto>(it)
                            throw KnownException(exceptionResponse, jsonElement.error ?: "")
                        }
                    }
                }
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