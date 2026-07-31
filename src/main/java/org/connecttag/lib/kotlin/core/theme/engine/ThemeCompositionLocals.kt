package org.connecttag.lib.kotlin.core.theme.engine

import androidx.compose.runtime.staticCompositionLocalOf
import org.connecttag.lib.kotlin.core.theme.branding.AppBrand
import org.connecttag.lib.kotlin.core.theme.branding.BrandColors

val LocalAppBrand = staticCompositionLocalOf<AppBrand> {
    error("No AppBrand provided")
}

val LocalBrandColors = staticCompositionLocalOf<BrandColors> {
    error("No BrandColors provided")
}

val LocalThemeSemanticColors = staticCompositionLocalOf {
    ThemeSemanticColors.unspecified
}

val LocalThemeMotion = staticCompositionLocalOf {
    ThemeMotion.Default
}
