package org.connecttag.lib.kotlin.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import org.connecttag.lib.kotlin.core.network.NetworkConnectivityProvider
import java.io.IOException

/**
 * An interceptor that checks for internet connectivity before proceeding with the request.
 * Throws [NoConnectivityException] if no internet is available.
 */
class ConnectivityInterceptor(
    private val connectivityProvider: NetworkConnectivityProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!connectivityProvider.isConnected()) {
            throw NoConnectivityException()
        }
        return chain.proceed(chain.request())
    }

    class NoConnectivityException : IOException("No internet connection available")
}
