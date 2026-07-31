package org.connecttag.lib.kotlin.core.connectivity

sealed interface ConnectivityState {
    data object Available : ConnectivityState
    data object AvailableThroughVpn : ConnectivityState
    data object Unavailable : ConnectivityState
    data object Losing : ConnectivityState
    data object Lost : ConnectivityState
}

fun ConnectivityState.isInternetAvailable(): Boolean {
    return this == ConnectivityState.Available || this == ConnectivityState.AvailableThroughVpn
}

enum class ConnectivityType {
    Wifi,
    Cellular,
    Ethernet,
    Vpn,
    Other,
    Unknown,
    None
}

data class ConnectivitySnapshot(
    val state: ConnectivityState,
    val type: ConnectivityType,
    val isMetered: Boolean = false,
) {
    val isInternetAvailable: Boolean get() = state.isInternetAvailable()
}
