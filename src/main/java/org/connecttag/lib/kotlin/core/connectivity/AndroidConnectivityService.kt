package org.connecttag.lib.kotlin.core.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart

class AndroidConnectivityService(private val context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val connectivityFlow: Flow<ConnectivitySnapshot> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                trySend(readSnapshot(capabilities))
            }

            override fun onLost(network: Network) {
                trySend(ConnectivitySnapshot(ConnectivityState.Lost, ConnectivityType.None))
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(callback)
        } else {
            val request = android.net.NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(request, callback)
        }

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.onStart {
        emit(getCurrentSnapshot())
    }.distinctUntilChanged()

    private fun getCurrentSnapshot(): ConnectivitySnapshot {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return readSnapshot(capabilities)
    }

    private fun readSnapshot(capabilities: NetworkCapabilities?): ConnectivitySnapshot {
        if (capabilities == null) return ConnectivitySnapshot(ConnectivityState.Unavailable, ConnectivityType.None)

        val hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        val type = when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectivityType.Wifi
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectivityType.Cellular
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ConnectivityType.Ethernet
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> ConnectivityType.Vpn
            else -> ConnectivityType.Other
        }

        val state = when {
            !hasInternet -> ConnectivityState.Unavailable
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> ConnectivityState.AvailableThroughVpn
            else -> ConnectivityState.Available
        }

        return ConnectivitySnapshot(
            state = state,
            type = type,
            isMetered = !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        )
    }
}
