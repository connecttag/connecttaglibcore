package org.connecttag.lib.kotlin.core.network

/**
 * Interface to provide dynamic domains for network requests.
 */
interface DomainProvider {
    /**
     * Returns the primary domain URL.
     */
    fun getPrimaryDomain(): String

    /**
     * Returns the backup/fallback domain URL.
     */
    fun getBackupDomain(): String
}
