package com.duyle.image_loader_hw8

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class NetworkReceiver : BroadcastReceiver() {

    // MutableStateFlow to manage connection status
    var isConnected by mutableStateOf(true)
        private set

    override fun onReceive(context: Context?, intent: Intent?) {
        // Checking network connectivity using NetworkCallback
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        // Check if there is a valid network connection (Wi-Fi or mobile data)
        isConnected = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}
