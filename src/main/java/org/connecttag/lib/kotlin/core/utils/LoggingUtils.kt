package org.connecttag.lib.kotlin.core.utils

import timber.log.Timber

/**
 * Utility for logging.
 */
object LoggingUtils {

    /**
     * Logs a debug message using Timber.
     */
    fun log(message: String, tag: String = "ConnectTag") {
        Timber.tag(tag).d(message)
    }

    /**
     * Logs an error message using Timber.
     */
    fun logError(message: String, tag: String = "ConnectTag", throwable: Throwable? = null) {
        Timber.tag(tag).e(throwable, message)
    }
}

/**
 * Global helper for quick logging.
 */
fun logDebug(message: String, tag: String = "ConnectTag") {
    LoggingUtils.log(message, tag)
}
