package org.connecttag.lib.kotlin.core.theme.engine

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Immutable
data class ThemeGradients(
    val primary: Brush,
    val secondary: Brush,
    val surface: Brush,
    val background: Brush
) {
    companion object {
        fun fromSeed(seed: Color, isDark: Boolean): ThemeGradients {
            return if (isDark) {
                ThemeGradients(
                    primary = Brush.verticalGradient(listOf(seed, seed.copy(alpha = 0.6f))),
                    secondary = Brush.verticalGradient(listOf(seed.copy(alpha = 0.4f), seed.copy(alpha = 0.2f))),
                    surface = Brush.verticalGradient(listOf(Color(0xFF2C2C2C), Color(0xFF1E1E1E))),
                    background = Brush.verticalGradient(listOf(Color(0xFF121212), Color(0xFF000000)))
                )
            } else {
                ThemeGradients(
                    primary = Brush.verticalGradient(listOf(seed, seed.copy(alpha = 0.8f))),
                    secondary = Brush.verticalGradient(listOf(seed.copy(alpha = 0.2f), seed.copy(alpha = 0.1f))),
                    surface = Brush.verticalGradient(listOf(Color.White, Color(0xFFF5F5F5))),
                    background = Brush.verticalGradient(listOf(Color(0xFFF9F9F9), Color.White))
                )
            }
        }
    }
}

val LocalConnectTagGradients = staticCompositionLocalOf {
    ThemeGradients.fromSeed(Color(0xFF6750A4), false)
}
