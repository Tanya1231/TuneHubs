package com.example.tunehubs.utils

import android.app.Application
import android.content.Context

class App : Application() {
    companion object {
        lateinit var context: Context

        fun isNetworkAvailable(): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}