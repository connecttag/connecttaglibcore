package org.connecttag.lib.kotlin.core.network.interceptor

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import org.connecttag.lib.kotlin.core.network.DomainProvider
import java.io.IOException

/**
 * An interceptor that handles automatic failover between primary and backup domains.
 */
class BaseUrlInterceptor(
    private val domainProvider: DomainProvider
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // List of domains to try in order
        val domainQueue = ArrayDeque<String>().apply {
            val primary = domainProvider.getPrimaryDomain()
            val backup = domainProvider.getBackupDomain()

            if (primary.isNotEmpty()) add(primary)
            if (backup.isNotEmpty() && backup != primary) add(backup)
        }

        // If no dynamic domains are provided, just proceed
        if (domainQueue.isEmpty()) {
            return chain.proceed(originalRequest)
        }

        var lastException: IOException? = null

        while (domainQueue.isNotEmpty()) {
            val currentDomain = domainQueue.removeFirst()

            try {
                val updatedUrl = adjustBaseUrl(originalRequest.url, currentDomain)
                val newRequest = if (updatedUrl != null) {
                    originalRequest.newBuilder().url(updatedUrl).build()
                } else {
                    originalRequest
                }

                val response = chain.proceed(newRequest)

                // If response is successful or a client error (4xx), return it.
                // We only failover on server errors (5xx) or network exceptions.
                if (response.isSuccessful || response.code < 500) {
                    return response
                } else {
                    response.close()
                }
            } catch (e: IOException) {
                lastException = e
                // Continue to next domain in queue
            }
        }

        throw lastException ?: IOException("Failed to connect to all provided domains")
    }

    private fun adjustBaseUrl(originalUrl: HttpUrl, newBaseUrl: String): HttpUrl? {
        return try {
            val newHttpUrl = newBaseUrl.toHttpUrl()
            originalUrl.newBuilder()
                .scheme(newHttpUrl.scheme)
                .host(newHttpUrl.host)
                .port(newHttpUrl.port)
                .build()
        } catch (e: Exception) {
            null
        }
    }
}
