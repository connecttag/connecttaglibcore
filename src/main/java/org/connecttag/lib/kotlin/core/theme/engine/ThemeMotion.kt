package org.connecttag.lib.kotlin.core.theme.engine

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.runtime.Immutable

@Immutable
data class ThemeMotion(
    val durationShort: Int = 150,
    val durationMedium: Int = 300,
    val durationLong: Int = 500,
    val easingStandard: Easing = FastOutSlowInEasing,
    val easingAccelerate: Easing = LinearOutSlowInEasing
) {
    companion object {
        val Snappy = ThemeMotion(
            durationShort = 100,
            durationMedium = 200,
            durationLong = 400
        )
        val Default = ThemeMotion()
    }
}
