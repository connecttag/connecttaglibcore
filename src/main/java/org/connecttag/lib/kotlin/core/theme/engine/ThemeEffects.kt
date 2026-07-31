package org.connecttag.lib.kotlin.core.theme.engine

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class ThemeEffects(
    val blurRadius: Dp = 10.dp,
    val glassAlpha: Float = 0.15f,
    val borderAlpha: Float = 0.2f
)

val LocalConnectTagEffects = staticCompositionLocalOf { ThemeEffects() }
