package org.connecttag.lib.kotlin.core.update

import kotlinx.coroutines.flow.Flow

/**
 * Interface to provide app status information from a repository or API.
 */
interface AppStatusProvider {
    /**
     * Returns a flow of [AppStatusInfo] to react to changes.
     */
    fun getAppStatusFlow(): Flow<AppStatusInfo?>

    /**
     * Returns the current [AppStatusInfo] directly.
     */
    fun getAppStatusSync(): AppStatusInfo?
}
