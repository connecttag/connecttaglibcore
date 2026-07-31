package org.connecttag.lib.kotlin.core.repository

import kotlinx.coroutines.flow.*
import org.connecttag.lib.kotlin.core.error.toAppError
import org.connecttag.lib.kotlin.core.result.PlatformResult

/**
 * A generic function that can provide a resource backed by both the sqlite database and the network.
 *
 * [ResultType] is the type of the Resource (Local Database object)
 * [RequestType] is the type of the Network response
 */
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(PlatformResult.Loading())
        try {
            saveFetchResult(fetch())
            query().map { PlatformResult.Success(it) }
        } catch (throwable: Throwable) {
            query().map { PlatformResult.Failure(throwable.toAppError()) }
        }
    } else {
        query().map { PlatformResult.Success(it) }
    }

    emitAll(flow)
}
