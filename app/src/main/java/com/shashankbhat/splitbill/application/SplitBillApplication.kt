package com.shashankbhat.splitbill.application

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.databinding.ObservableField
import com.shashankbhat.splitbill.util.NetworkCheck
import com.shashankbhat.splitbill.util.NetworkStatus
import com.shashankbhat.splitbill.util.NetworkStatus.Companion.setAvailable
import com.shashankbhat.splitbill.util.NetworkStatus.Companion.setUnavailable
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SplitBillApplication : Application() {
    val observeNetworkStatus = ObservableField<NetworkStatus>()

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback : ConnectivityManager.NetworkCallback
    override fun onCreate() {
        super.onCreate()

        connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager

        if(NetworkCheck.isInternetAvailable(applicationContext)){
            observeNetworkStatus.setAvailable()
        } else {
            observeNetworkStatus.setUnavailable()
        }

        listenToNetworkChanges()
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
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                observeNetworkStatus.setUnavailable()
            }
        }
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}