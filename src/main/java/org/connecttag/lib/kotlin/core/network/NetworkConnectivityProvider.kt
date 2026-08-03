package org.connecttag.lib.kotlin.core.network

/**
 * Interface to check network connectivity status.
 */
interface NetworkConnectivityProvider {
    /**
     * Returns true if there is an active internet connection.
     */
    fun isConnected(): Boolean
}
