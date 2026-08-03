package org.connecttag.lib.kotlin.core.actions

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * CompositionLocal for [ActionProcessor].
 */
val LocalActionProcessor = staticCompositionLocalOf<ActionProcessor> {
    error("No ActionProcessor provided")
}
