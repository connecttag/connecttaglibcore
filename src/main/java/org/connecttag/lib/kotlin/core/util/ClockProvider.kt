package org.connecttag.lib.kotlin.core.util

/**
 * Interface to provide the current time.
 */
interface ClockProvider {
    /**
     * Returns the current time in milliseconds since epoch.
     */
    fun nowMillis(): Long
}

/**
 * Platform clock implementation.
 */
object SystemClockProvider : ClockProvider {
    override fun nowMillis(): Long = System.currentTimeMillis()
}
