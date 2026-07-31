package org.connecttag.lib.kotlin.core.utils

import kotlinx.coroutines.withContext

/**
 * A base Repository class that provides common network and data handling logic.
 */
abstract class BaseRepository(
    protected val dispatchers: DispatcherProvider = DefaultDispatcherProvider
) {
    /**
     * Executes a network or data call safely within the IO dispatcher.
     */
    protected suspend fun <T> safeCall(
        call: suspend () -> T
    ): Result<T> = withContext(dispatchers.io) {
        try {
            Result.success(call())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
