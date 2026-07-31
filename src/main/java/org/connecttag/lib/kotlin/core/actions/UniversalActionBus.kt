package org.connecttag.lib.kotlin.core.actions

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.connecttag.lib.kotlin.core.util.ClockProvider
import org.connecttag.lib.kotlin.core.util.SystemClockProvider

/**
 * Central bus for all user and system actions.
 * Used for tracking navigation breadcrumbs and debugging.
 */
class UniversalActionBus(
    private val clock: ClockProvider = SystemClockProvider,
) {
    private val _actions = MutableSharedFlow<ActionBreadcrumb>(extraBufferCapacity = 64)
    val actions = _actions.asSharedFlow()

    /**
     * Reports an action to the bus.
     */
    fun reportAction(name: String, details: Map<String, String> = emptyMap()) {
        _actions.tryEmit(
            ActionBreadcrumb(
                name = name,
                details = details,
                timestamp = clock.nowMillis(),
            ),
        )
    }
}

/**
 * Represents a single tracked action in the app.
 */
data class ActionBreadcrumb(
    val name: String,
    val details: Map<String, String>,
    val timestamp: Long,
)
