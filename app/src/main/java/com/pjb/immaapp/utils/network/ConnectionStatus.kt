package com.pjb.immaapp.utils.network

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService

@Suppress("DEPRECATION")
class ConnectionStatus {
    companion object {
        fun isConnected(context: Context): Boolean {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val connection = manager.activeNetworkInfo
            return connection != null && connection.isConnectedOrConnecting
        }
    }
}