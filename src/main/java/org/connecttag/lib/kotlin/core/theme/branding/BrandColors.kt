package org.connecttag.lib.kotlin.core.theme.branding

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import kotlin.math.abs

interface BrandColors {
    val primary: Color
    val onPrimary: Color
    val primaryContainer: Color
    val onPrimaryContainer: Color

    val secondary: Color
    val onSecondary: Color
    val secondaryContainer: Color
    val onSecondaryContainer: Color

    val tertiary: Color
    val onTertiary: Color
    val tertiaryContainer: Color
    val onTertiaryContainer: Color

    val error: Color
    val onError: Color

    val background: Color
    val onBackground: Color

    val surface: Color
    val onSurface: Color
    val surfaceVariant: Color
    val onSurfaceVariant: Color

    val outline: Color

    fun toColorScheme(isDark: Boolean = false): ColorScheme {
        return if (isDark) {
            darkColorScheme(
                primary = primary,
                onPrimary = onPrimary,
                primaryContainer = primaryContainer,
                onPrimaryContainer = onPrimaryContainer,
                secondary = secondary,
                onSecondary = onSecondary,
                secondaryContainer = secondaryContainer,
                onSecondaryContainer = onSecondaryContainer,
                tertiary = tertiary,
                onTertiary = onTertiary,
                tertiaryContainer = tertiaryContainer,
                onTertiaryContainer = onTertiaryContainer,
                error = error,
                onError = onError,
                background = background,
                onBackground = onBackground,
                surface = surface,
                onSurface = onSurface,
                surfaceVariant = surfaceVariant,
                onSurfaceVariant = onSurfaceVariant,
                outline = outline
            )
        } else {
            lightColorScheme(
                primary = primary,
                onPrimary = onPrimary,
                primaryContainer = primaryContainer,
                onPrimaryContainer = onPrimaryContainer,
                secondary = secondary,
                onSecondary = onSecondary,
                secondaryContainer = secondaryContainer,
                onSecondaryContainer = onSecondaryContainer,
                tertiary = tertiary,
                onTertiary = onTertiary,
                tertiaryContainer = tertiaryContainer,
                onTertiaryContainer = onTertiaryContainer,
                error = error,
                onError = onError,
                background = background,
                onBackground = onBackground,
                surface = surface,
                onSurface = onSurface,
                surfaceVariant = surfaceVariant,
                onSurfaceVariant = onSurfaceVariant,
                outline = outline
            )
        }
    }

    companion object {
        /**
         * Generates a full BrandColors set from a single seed color.
         */
        fun fromSeed(seed: Color, isDark: Boolean = false): BrandColors {
            val hsv = seed.toHsv()
            val h = hsv.hue
            val s = hsv.saturation

            return if (isDark) {
                GeneratedBrandColors(
                    primary = fromHSV(h, s, 0.8f),
                    onPrimary = fromHSV(h, s, 0.2f),
                    primaryContainer = fromHSV(h, s, 0.3f),
                    onPrimaryContainer = fromHSV(h, s, 0.9f),
                    secondary = fromHSV(h, s * 0.3f, 0.7f),
                    onSecondary = fromHSV(h, s * 0.3f, 0.2f),
                    secondaryContainer = fromHSV(h, s * 0.3f, 0.3f),
                    onSecondaryContainer = fromHSV(h, s * 0.3f, 0.9f),
                    tertiary = fromHSV((h + 60) % 360, s * 0.5f, 0.7f),
                    onTertiary = fromHSV((h + 60) % 360, s * 0.5f, 0.2f),
                    tertiaryContainer = fromHSV((h + 60) % 360, s * 0.5f, 0.3f),
                    onTertiaryContainer = fromHSV((h + 60) % 360, s * 0.5f, 0.9f),
                    error = Color(0xFFCF6679),
                    onError = Color.Black,
                    background = Color(0xFF121212),
                    onBackground = Color(0xFFE1E1E1),
                    surface = Color(0xFF121212),
                    onSurface = Color(0xFFE1E1E1),
                    surfaceVariant = Color(0xFF333333),
                    onSurfaceVariant = Color(0xFFCCCCCC),
                    outline = Color(0xFF999999)
                )
            } else {
                GeneratedBrandColors(
                    primary = seed,
                    onPrimary = Color.White,
                    primaryContainer = fromHSV(h, s, 0.9f),
                    onPrimaryContainer = fromHSV(h, s, 0.1f),
                    secondary = fromHSV(h, s * 0.3f, 0.4f),
                    onSecondary = Color.White,
                    secondaryContainer = fromHSV(h, s * 0.3f, 0.9f),
                    onSecondaryContainer = fromHSV(h, s * 0.3f, 0.1f),
                    tertiary = fromHSV((h + 60) % 360, s * 0.5f, 0.5f),
                    onTertiary = Color.White,
                    tertiaryContainer = fromHSV((h + 60) % 360, s * 0.5f, 0.9f),
                    onTertiaryContainer = fromHSV((h + 60) % 360, s * 0.5f, 0.1f),
                    error = Color(0xFFB00020),
                    onError = Color.White,
                    background = Color(0xFFF9F9F9),
                    onBackground = Color(0xFF1C1C1C),
                    surface = Color.White,
                    onSurface = Color(0xFF1C1C1C),
                    surfaceVariant = Color(0xFFEEEEEE),
                    onSurfaceVariant = Color(0xFF444444),
                    outline = Color(0xFF777777)
                )
            }
        }

        private fun fromHSV(h: Float, s: Float, v: Float): Color {
            val hue = ((h % 360f) + 360f) % 360f
            val saturation = s.coerceIn(0f, 1f)
            val value = v.coerceIn(0f, 1f)
            val chroma = value * saturation
            val x = chroma * (1f - abs((hue / 60f) % 2f - 1f))
            val m = value - chroma
            val (red, green, blue) = when {
                hue < 60f -> Triple(chroma, x, 0f)
                hue < 120f -> Triple(x, chroma, 0f)
                hue < 180f -> Triple(0f, chroma, x)
                hue < 240f -> Triple(0f, x, chroma)
                hue < 300f -> Triple(x, 0f, chroma)
                else -> Triple(chroma, 0f, x)
            }
            return Color(
                red = red + m,
                green = green + m,
                blue = blue + m,
            )
        }

        private fun Color.toHsv(): Hsv {
            val max = maxOf(red, green, blue)
            val min = minOf(red, green, blue)
            val delta = max - min
            val hue = when {
                delta == 0f -> 0f
                max == red -> (60f * ((green - blue) / delta % 6f) + 360f) % 360f
                max == green -> 60f * ((blue - red) / delta + 2f)
                else -> 60f * ((red - green) / delta + 4f)
            }
            val saturation = if (max == 0f) 0f else delta / max
            return Hsv(hue = hue, saturation = saturation, value = max)
        }
    }
}

private data class Hsv(
    val hue: Float,
    val saturation: Float,
    val value: Float,
)

private class GeneratedBrandColors(
    override val primary: Color,
    override val onPrimary: Color,
    override val primaryContainer: Color,
    override val onPrimaryContainer: Color,
    override val secondary: Color,
    override val onSecondary: Color,
    override val secondaryContainer: Color,
    override val onSecondaryContainer: Color,
    override val tertiary: Color,
    override val onTertiary: Color,
    override val tertiaryContainer: Color,
    override val onTertiaryContainer: Color,
    override val error: Color,
    override val onError: Color,
    override val background: Color,
    override val onBackground: Color,
    override val surface: Color,
    override val onSurface: Color,
    override val surfaceVariant: Color,
    override val onSurfaceVariant: Color,
    override val outline: Color
) : BrandColors
