package com.shashankbhat.splitbill.application

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.databinding.ObservableField
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.shashankbhat.splitbill.util.NetworkCheck
import com.shashankbhat.splitbill.util.NetworkStatus
import com.shashankbhat.splitbill.util.NetworkStatus.Companion.setAvailable
import com.shashankbhat.splitbill.util.NetworkStatus.Companion.setUnavailable
import com.shashankbhat.splitbill.worker.MoveOfflineDataToOnline
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class SplitBillApplication : Application(), Configuration.Provider {
    val observeNetworkStatus = ObservableField<NetworkStatus>()

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback : ConnectivityManager.NetworkCallback

    private lateinit var workManager: WorkManager
    private var constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()


    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override fun onCreate() {
        super.onCreate()

        connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager

        val myConfig: Configuration = Configuration.Builder()
            .build()
        WorkManager.initialize(this, myConfig)
        workManager = WorkManager
            .getInstance(this)

        if(NetworkCheck.isInternetAvailable(applicationContext)){
            observeNetworkStatus.setAvailable()
        } else {
            observeNetworkStatus.setUnavailable()
            scheduleMoveOfflineData()
        }

        listenToNetworkChanges()

        scheduleMoveOfflineData()
    }

    private fun listenToNetworkChanges() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onUnavailable() {
                super.onUnavailable()
                observeNetworkStatus.setUnavailable()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                observeNetworkStatus.setUnavailable()
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                observeNetworkStatus.setAvailable()
                scheduleMoveOfflineData()
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                observeNetworkStatus.setUnavailable()
            }
        }
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    private fun scheduleMoveOfflineData(){
        val moveOfflineDataToOnline = OneTimeWorkRequestBuilder<MoveOfflineDataToOnline>()
//            .setConstraints(constraints)
            .build()
        workManager.enqueue(moveOfflineDataToOnline)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}