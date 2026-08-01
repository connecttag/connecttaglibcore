package org.connecttag.lib.kotlin.core.utils

import org.connecttag.lib.kotlin.core.coroutine.DispatcherProvider
import org.connecttag.lib.kotlin.core.coroutine.DefaultDispatcherProvider
import org.connecttag.lib.kotlin.core.error.toAppError
import org.connecttag.lib.kotlin.core.result.PlatformResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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

    /**
     * Wraps a data call into a [Flow] that emits [PlatformResult].
     */
    protected fun <T> safeFlow(
        call: suspend () -> T
    ): Flow<PlatformResult<T>> = flow {
        emit(PlatformResult.Loading())
        try {
            val result = call()
            emit(PlatformResult.Success(result))
        } catch (e: Exception) {
            emit(PlatformResult.Failure(e.toAppError()))
        }
    }.flowOn(dispatchers.io)
}
