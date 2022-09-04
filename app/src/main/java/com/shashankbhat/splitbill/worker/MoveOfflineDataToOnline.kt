package com.shashankbhat.splitbill.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shashankbhat.splitbill.database.local.SplitBillDatabase
import com.shashankbhat.splitbill.database.local.repository.GroupRepository
import com.shashankbhat.splitbill.database.local.repository.UserRepository
import com.shashankbhat.splitbill.database.remote.repository.GroupRepositoryRemote
import com.shashankbhat.splitbill.di.AppModule
import com.shashankbhat.splitbill.util.KnownException
import com.shashankbhat.splitbill.util.KnownExceptionDto
import com.shashankbhat.splitbill.util.extension.sendMessages
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@HiltWorker
class MoveOfflineDataToOnline @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    lateinit var groupRepoRemote: GroupRepositoryRemote
    override suspend fun doWork(): Result = try {

        val db = SplitBillDatabase.invoke(context)

        groupRepoRemote = GroupRepositoryRemote(HttpClient {
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
                            val jsonElement = Json.decodeFromString<KnownExceptionDto>(it)
                            throw KnownException(exceptionResponse, jsonElement.error ?: "")
                        }
                    }
                }
            }
        },
            groupRepository = GroupRepository(groupDao = db.groupDao(), userDao = db.userDao()),
            userRepository = UserRepository( db.userDao()),
            sharedPreferences = context.getSharedPreferences(AppModule.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        )
        groupRepoRemote.saveAllUnsavedGroups()
        applicationContext.sendMessages("REFRESH_GROUP")

        Result.success()
    } catch (error: Exception) {
        Result.failure()
    }
}