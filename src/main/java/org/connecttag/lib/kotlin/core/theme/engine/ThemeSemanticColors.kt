package org.connecttag.lib.kotlin.core.theme.engine

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ThemeSemanticColors(
    val success: Color,
    val warning: Color,
    val info: Color,
    val verified: Color,
    val error: Color,
    val whatsApp: Color = Color(0xFF25D366),
    val telegram: Color = Color(0xFF24A1DE),
) {
    companion object {
        fun fromColorScheme(colorScheme: ColorScheme): ThemeSemanticColors =
            ThemeSemanticColors(
                success = Color(0xFF22B573),
                warning = Color(0xFFF59E0B),
                info = Color(0xFF3B82F6),
                verified = colorScheme.primary,
                error = colorScheme.error,
            )

        val unspecified = ThemeSemanticColors(
            success = Color.Unspecified,
            warning = Color.Unspecified,
            info = Color.Unspecified,
            verified = Color.Unspecified,
            error = Color.Unspecified,
        )
    }
}
