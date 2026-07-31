package org.connecttag.lib.kotlin.core.utils

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.os.LocaleListCompat
import org.connecttag.lib.kotlin.core.theme.AppLanguage
import org.connecttag.lib.kotlin.core.theme.ThemeSettings

/**
 * Provides locale and layout direction based on the current [AppLanguage].
 */
@Composable
fun ConnectTagLocaleProvider(
    content: @Composable () -> Unit
) {
    val language = ThemeSettings.appLanguage
    val systemLayoutDirection = LocalLayoutDirection.current

    LaunchedEffect(language) {
        val localeList = if (language == AppLanguage.SYSTEM) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(language.value)
        }
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    val layoutDirection = when (language) {
        AppLanguage.ARABIC -> LayoutDirection.Rtl
        AppLanguage.ENGLISH -> LayoutDirection.Ltr
        else -> systemLayoutDirection
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides layoutDirection,
        content = content
    )
}
