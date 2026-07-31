package org.connecttag.lib.kotlin.core.result

import org.connecttag.lib.kotlin.core.error.AppError
import org.connecttag.lib.kotlin.core.error.toAppError

sealed interface PlatformResult<out T> {
    data object Idle : PlatformResult<Nothing>
    data object Empty : PlatformResult<Nothing>
    data class Loading(val loading: PlatformLoading = PlatformLoading()) : PlatformResult<Nothing>
    data class Success<T>(val data: T) : PlatformResult<T>
    data class Failure(val error: AppError) : PlatformResult<Nothing>
}

data class PlatformLoading(
    val message: String = "",
    val note: String? = null,
    val blocking: Boolean = true,
)

val PlatformResult<*>.isLoading: Boolean
    get() = this is PlatformResult.Loading

fun PlatformResult<*>.errorMessageOrEmpty(): String {
    return (this as? PlatformResult.Failure)?.error?.message.orEmpty()
}

fun PlatformResult<*>.errorMessageKeyOrNull(): String? {
    return (this as? PlatformResult.Failure)?.error?.messageKey
}

fun <T> PlatformResult<T>.dataOrNull(): T? {
    return when (this) {
        is PlatformResult.Success -> data
        else -> null
    }
}

inline fun <T, R> PlatformResult<T>.fold(
    onIdle: () -> R,
    onEmpty: () -> R,
    onLoading: (PlatformLoading) -> R,
    onSuccess: (T) -> R,
    onFailure: (AppError) -> R,
): R {
    return when (this) {
        PlatformResult.Idle -> onIdle()
        PlatformResult.Empty -> onEmpty()
        is PlatformResult.Loading -> onLoading(loading)
        is PlatformResult.Success -> onSuccess(data)
        is PlatformResult.Failure -> onFailure(error)
    }
}

inline fun <T, R> PlatformResult<T>.map(
    transform: (T) -> R,
): PlatformResult<R> {
    return when (this) {
        PlatformResult.Idle -> PlatformResult.Idle
        PlatformResult.Empty -> PlatformResult.Empty
        is PlatformResult.Loading -> this
        is PlatformResult.Success -> PlatformResult.Success(transform(data))
        is PlatformResult.Failure -> this
    }
}

inline fun <T> PlatformResult<T>.mapError(
    transform: (AppError) -> AppError,
): PlatformResult<T> {
    return when (this) {
        is PlatformResult.Failure -> PlatformResult.Failure(transform(error))
        else -> this
    }
}

inline fun <T> PlatformResult<T>.recover(
    transform: (AppError) -> T,
): PlatformResult<T> {
    return when (this) {
        is PlatformResult.Failure -> PlatformResult.Success(transform(error))
        else -> this
    }
}

inline fun <T> PlatformResult<T>.getOrElse(
    defaultValue: (AppError?) -> T,
): T {
    return when (this) {
        is PlatformResult.Success -> data
        is PlatformResult.Failure -> defaultValue(error)
        else -> defaultValue(null)
    }
}

fun <T> Result<T>.toPlatformResult(
    errorMapper: (Throwable) -> AppError = { it.toAppError() },
): PlatformResult<T> {
    return fold(
        onSuccess = { PlatformResult.Success(it) },
        onFailure = { PlatformResult.Failure(errorMapper(it)) },
    )
}
