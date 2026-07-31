package org.connecttag.lib.kotlin.core.error

/**
 * Standard utility to map any [Throwable] to a structured [AppError].
 */
fun Throwable.toAppError(
    fallbackKey: String = AppErrorKeys.UNKNOWN,
    fallbackKind: ErrorKind = ErrorKind.Unknown,
    fallbackSeverity: ErrorSeverity = ErrorSeverity.Error
): AppError {
    return when (this) {
        is AppError -> this
        else -> {
            val key = this.resolveErrorKey() ?: fallbackKey
            val kind = this.resolveErrorKind() ?: fallbackKind
            AppError(
                key = key,
                message = this.message,
                throwable = this,
                kind = kind,
                severity = fallbackSeverity,
                retryable = kind.isRetryable()
            )
        }
    }
}

private fun Throwable.resolveErrorKey(): String? {
    return when {
        isNetworkException() -> AppErrorKeys.NETWORK_ERROR
        isTimeoutException() -> AppErrorKeys.TIMEOUT
        else -> null
    }
}

private fun Throwable.resolveErrorKind(): ErrorKind? {
    return when {
        isNetworkException() -> ErrorKind.Network
        isTimeoutException() -> ErrorKind.Timeout
        else -> null
    }
}

private fun ErrorKind.isRetryable(): Boolean {
    return when (this) {
        ErrorKind.Network,
        ErrorKind.Timeout,
        ErrorKind.Server,
        ErrorKind.LocalData -> true
        else -> false
    }
}

private fun Throwable.isNetworkException(): Boolean {
    val name = this::class.simpleName ?: ""
    return name in NETWORK_EXCEPTION_NAMES ||
           name.endsWith("IOException") ||
           name.contains("ConnectException") ||
           name.contains("HostException")
}

private fun Throwable.isTimeoutException(): Boolean {
    val name = this::class.simpleName ?: ""
    return name in TIMEOUT_EXCEPTION_NAMES ||
           name.contains("Timeout")
}

private val NETWORK_EXCEPTION_NAMES = setOf(
    "ConnectException",
    "NoNetworkException",
    "NoRouteToHostException",
    "ProtocolException",
    "SocketException",
    "SSLException",
    "SSLHandshakeException",
    "UnknownHostException",
)

private val TIMEOUT_EXCEPTION_NAMES = setOf(
    "HttpTimeoutException",
    "SocketTimeoutException",
    "TimeoutException",
    "TimeoutCancellationException",
)
