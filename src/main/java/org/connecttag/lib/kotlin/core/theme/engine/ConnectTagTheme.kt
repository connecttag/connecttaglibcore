package org.connecttag.lib.kotlin.core.theme.engine

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import org.connecttag.lib.kotlin.core.theme.ThemeMode
import org.connecttag.lib.kotlin.core.theme.ThemeSettings
import org.connecttag.lib.kotlin.core.theme.branding.BrandColors
import org.connecttag.lib.kotlin.core.theme.branding.AppBrand

@Composable
fun ConnectTagTheme(
    brand: AppBrand = ThemeSettings.selectedBrand,
    darkTheme: Boolean = when (ThemeSettings.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    },
    dynamicColor: Boolean = ThemeSettings.isDynamicColorEnabled,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    // Seed color logic: Custom Seed > Brand Primary
    val seedColor = ThemeSettings.customSeedColor ?: brand.colors.primary

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ThemeSettings.customSeedColor == null -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> {
            // Generate full scheme from seed if dynamic is off or custom seed is set
            BrandColors.fromSeed(seedColor, darkTheme).toColorScheme(darkTheme)
        }
    }

    val semanticColors = ThemeSemanticColors.fromColorScheme(colorScheme)
    val gradients = ThemeGradients.fromSeed(colorScheme.primary, darkTheme)
    val spacing = ThemeSpacing()
    val effects = ThemeEffects()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = context.findActivity()?.window
            if (window != null) {
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()
                
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    window.isNavigationBarContrastEnforced = false
                }
                
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !darkTheme
                controller.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = {
            CompositionLocalProvider(
                LocalAppBrand provides brand,
                LocalBrandColors provides brand.colors,
                LocalThemeSemanticColors provides semanticColors,
                LocalThemeMotion provides brand.motion,
                LocalConnectTagGradients provides gradients,
                LocalConnectTagSpacing provides spacing,
                LocalConnectTagEffects provides effects,
                content = content
            )
        }
    )
}

private fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

/**
 * Global access to ConnectTag design tokens.
 */
object ConnectTagTheme {
    val brand: AppBrand @Composable get() = LocalAppBrand.current
    val colors: BrandColors @Composable get() = LocalBrandColors.current
    val semanticColors: ThemeSemanticColors @Composable get() = LocalThemeSemanticColors.current
    val gradients: ThemeGradients @Composable get() = LocalConnectTagGradients.current
    val spacing: ThemeSpacing @Composable get() = LocalConnectTagSpacing.current
    val effects: ThemeEffects @Composable get() = LocalConnectTagEffects.current
}
