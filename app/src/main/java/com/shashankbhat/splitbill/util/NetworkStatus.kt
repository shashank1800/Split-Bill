package com.shashankbhat.splitbill.util

import androidx.databinding.ObservableField

data class NetworkStatus(
        val currStatus: Network = Network.Unavailable,
        val prevStatus: Network = Network.Unavailable,
) {

    fun isAvailable(): Boolean {
        return currStatus == Network.Available
    }

    fun isLoosing(): Boolean {
        return currStatus == Network.Loosing
    }

    fun isUnavailable(): Boolean {
        return currStatus == Network.Unavailable
    }

    companion object {
        fun ObservableField<NetworkStatus>.setAvailable() {
            set(NetworkStatus(Network.Available, this.get()?.currStatus ?: Network.Unavailable))
        }

        fun ObservableField<NetworkStatus>.setLoosing() {
            set(NetworkStatus(Network.Loosing, this.get()?.currStatus ?: Network.Unavailable))
        }

        fun ObservableField<NetworkStatus>.setUnavailable() {
            set(NetworkStatus(Network.Unavailable, this.get()?.currStatus ?: Network.Unavailable))
        }
    }

    enum class Network {
        Loosing,
        Unavailable,
        Available
    }
}

