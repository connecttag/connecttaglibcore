package org.connecttag.lib.kotlin.core.error

/**
 * Categorizes errors to help the presentation layer decide how to display or recover.
 */
enum class ErrorKind {
    Network,
    Authentication,
    Authorization,
    Timeout,
    Validation,
    NotFound,
    Server,
    LocalData,
    Permission,
    SessionExpired,
    Unknown,
}

/**
 * Indicates the priority or impact of the error.
 */
enum class ErrorSeverity {
    Info,
    Warning,
    Error,
    Critical,
}

/**
 * Represents a domain-level error with a machine-readable key.
 */
open class AppError(
    open val key: String,
    override val message: String? = null,
    open val messageKey: String? = null,
    open val throwable: Throwable? = null,
    open val kind: ErrorKind = ErrorKind.Unknown,
    open val severity: ErrorSeverity = ErrorSeverity.Error,
    open val retryable: Boolean = true
) : Exception(message, throwable) {

    fun copy(
        key: String = this.key,
        message: String? = this.message,
        messageKey: String? = this.messageKey,
        throwable: Throwable? = this.throwable,
        kind: ErrorKind = this.kind,
        severity: ErrorSeverity = this.severity,
        retryable: Boolean = this.retryable
    ): AppError = AppError(key, message, messageKey, throwable, kind, severity, retryable)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AppError) return false
        return key == other.key &&
               message == other.message &&
               messageKey == other.messageKey &&
               throwable == other.throwable &&
               kind == other.kind &&
               severity == other.severity &&
               retryable == other.retryable
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + (message?.hashCode() ?: 0)
        result = 31 * result + (messageKey?.hashCode() ?: 0)
        result = 31 * result + (throwable?.hashCode() ?: 0)
        result = 31 * result + kind.hashCode()
        result = 31 * result + severity.hashCode()
        result = 31 * result + retryable.hashCode()
        return result
    }

    override fun toString(): String {
        return "AppError(key='$key', messageKey=$messageKey, kind=$kind, severity=$severity, retryable=$retryable, message=$message, throwable=$throwable)"
    }
}

/**
 * Common error keys used across the application.
 */
object AppErrorKeys {
    const val UNKNOWN = "UNKNOWN_ERROR"
    const val NETWORK_ERROR = "NETWORK_ERROR"
    const val TIMEOUT = "TIMEOUT"
    const val NOT_FOUND = "NOT_FOUND"
    const val ACCESS_DENIED = "ACCESS_DENIED"
    const val SERVER_UNAVAILABLE = "SERVER_UNAVAILABLE"
    const val INVALID_DATA = "INVALID_DATA"
    const val LOCAL_STORAGE = "LOCAL_STORAGE"
    const val UNAUTHORIZED = "UNAUTHORIZED"
    const val FORBIDDEN = "FORBIDDEN"
    const val VALIDATION_FAILED = "VALIDATION_FAILED"
    const val SESSION_EXPIRED = "SESSION_EXPIRED"
}
