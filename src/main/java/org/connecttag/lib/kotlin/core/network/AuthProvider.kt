package org.connecttag.lib.kotlin.core.network

/**
 * Interface to provide authentication data for network requests.
 */
interface AuthProvider {
    /**
     * Returns the authentication token (e.g., Bearer token).
     */
    fun getAuthToken(): String

    /**
     * Returns the API key.
     */
    fun getApiKey(): String

    /**
     * Returns true if the user is currently authenticated.
     */
    fun isAuthenticated(): Boolean
}
