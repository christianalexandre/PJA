package com.example.conversaomoedas.classes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.conversaomoedas.home_screen.HomeScreenViewModel

class InternetConnectionListener(private val context: Context, var availableCurrenciesMap: Map<String, String>, private val homeScreenViewModel: HomeScreenViewModel) {

    private var networkReceiver: BroadcastReceiver? = null

    fun startListening() {
        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (isInternetAvailable(context) && availableCurrenciesMap.isEmpty()) {
                    homeScreenViewModel.setAvailableCurrenciesMap()
                }
            }
        }

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkReceiver, filter)
    }

    fun stopListening() {
        networkReceiver?.let {
            context.unregisterReceiver(it)
            networkReceiver = null
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}
