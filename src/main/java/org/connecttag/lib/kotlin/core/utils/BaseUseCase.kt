package org.connecttag.lib.kotlin.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Base interface for Use Cases (Domain Layer).
 */
abstract class BaseUseCase<in P, out R>(
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider
) {
    /**
     * Executes the use case logic.
     */
    abstract suspend fun execute(parameters: P): Result<R>

    /**
     * Invokes the use case.
     */
    suspend operator fun invoke(parameters: P): Result<R> = execute(parameters)
}

/**
 * Base interface for Use Cases that return a [Flow].
 */
abstract class BaseFlowUseCase<in P, out R>(
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider
) {
    /**
     * Creates the flow for this use case.
     */
    abstract fun createFlow(parameters: P): Flow<Result<R>>

    /**
     * Invokes the use case and ensures it runs on the IO dispatcher.
     */
    operator fun invoke(parameters: P): Flow<Result<R>> =
        createFlow(parameters).flowOn(dispatchers.io)
}
