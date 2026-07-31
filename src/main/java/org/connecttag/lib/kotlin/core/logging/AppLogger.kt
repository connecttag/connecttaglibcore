package org.connecttag.lib.kotlin.core.logging

import io.github.aakira.napier.Napier

/**
 * Standard interface for application-wide logging.
 */
object AppLogger {
    fun d(message: String, category: LogCategory = LogCategory.General, throwable: Throwable? = null) {
        Napier.d(message, throwable, category.tag)
    }

    fun i(message: String, category: LogCategory = LogCategory.General, throwable: Throwable? = null) {
        Napier.i(message, throwable, category.tag)
    }

    fun w(message: String, category: LogCategory = LogCategory.General, throwable: Throwable? = null) {
        Napier.w(message, throwable, category.tag)
    }

    fun e(message: String, category: LogCategory = LogCategory.General, throwable: Throwable? = null) {
        Napier.e(message, throwable, category.tag)
    }
}
