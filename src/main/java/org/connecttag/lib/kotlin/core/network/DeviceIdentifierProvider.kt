package org.connecttag.lib.kotlin.core.network

/**
 * Interface to provide unique device and application identifiers.
 */
interface DeviceIdentifierProvider {
    /**
     * Returns a unique device identifier (e.g., Android ID).
     */
    fun getDeviceId(): String

    /**
     * Returns the application version name.
     */
    fun getAppVersion(): String

    /**
     * Returns the application version code as a string.
     */
    fun getAppVersionCode(): String
}
