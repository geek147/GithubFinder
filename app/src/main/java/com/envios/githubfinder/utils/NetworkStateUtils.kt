package com.envios.githubfinder.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData


class NetworkStateUtil(context: Context) : LiveData<Boolean?>() {
    private val mContext: Context = context
    private var networkCallback: NetworkCallback? = null
    private var networkReceiver: NetworkReceiver? = null
    private val connectivityManager: ConnectivityManager?

    override fun onActive() {
        super.onActive()
        updateConnection()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                connectivityManager!!.registerDefaultNetworkCallback(networkCallback)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val networkRequest = NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build()
                connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
            }
            else -> {
                mContext.registerReceiver(
                    networkReceiver,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
        } else {
            mContext.unregisterReceiver(networkReceiver)
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    internal class NetworkCallback(networkStateUtil: NetworkStateUtil) :
        ConnectivityManager.NetworkCallback() {
        private val networkState: NetworkStateUtil = networkStateUtil
        override fun onAvailable(network: Network) {
            networkState.postValue(true)
        }

        override fun onLost(network: Network) {
            networkState.postValue(false)
        }

    }

    private fun updateConnection() {
        if (connectivityManager != null) {
            val activeNetwork = connectivityManager.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
                postValue(true)
            } else {
                postValue(false)
            }
        }
    }

    internal inner class NetworkReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
                updateConnection()

        }
    }

    init {
        connectivityManager =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkCallback = NetworkCallback(this)
        } else {
            networkReceiver = NetworkReceiver()
        }
    }
}