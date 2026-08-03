package org.connecttag.lib.kotlin.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import org.connecttag.lib.kotlin.core.network.AuthProvider

/**
 * An interceptor that adds Authorization and API Key headers.
 */
class AuthInterceptor(
    private val authProvider: AuthProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        val apiKey = authProvider.getApiKey()
        if (apiKey.isNotEmpty()) {
            requestBuilder.addHeader("X-Api-Key", apiKey)
            requestBuilder.addHeader("Api-Key", apiKey)
        }

        val token = authProvider.getAuthToken()
        if (token.isNotEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
