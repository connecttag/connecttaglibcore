package org.connecttag.lib.kotlin.core.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.SettingsSuggest
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.connecttag.lib.kotlin.core.R
import org.connecttag.lib.kotlin.core.theme.AppLanguage
import org.connecttag.lib.kotlin.core.theme.ThemeSettings

/**
 * Builds the Language (اللغة) setting section with System Default, Arabic, and English.
 *
 * @param currentLanguage Current selected AppLanguage (defaults to ThemeSettings.appLanguage)
 * @param onLanguageChanged Callback when language is selected
 */
@Composable
fun languageSettingsSection(
    currentLanguage: AppLanguage = ThemeSettings.appLanguage,
    onLanguageChanged: (AppLanguage) -> Unit = { ThemeSettings.appLanguage = it }
): SettingSection {
    val languageSummary = when (currentLanguage) {
        AppLanguage.SYSTEM -> stringResource(R.string.system_default)
        AppLanguage.ARABIC -> stringResource(R.string.arabic)
        AppLanguage.ENGLISH -> stringResource(R.string.english)
    }

    val languageIcon = when (currentLanguage) {
        AppLanguage.SYSTEM -> Icons.Rounded.SettingsSuggest
        AppLanguage.ARABIC -> Icons.Rounded.Language
        AppLanguage.ENGLISH -> Icons.Rounded.Translate
    }

    val languageOptions = listOf(
        SelectionOption(
            value = AppLanguage.SYSTEM.name,
            title = stringResource(R.string.system_default),
            icon = Icons.Rounded.SettingsSuggest,
            summary = stringResource(R.string.language_system_desc)
        ),
        SelectionOption(
            value = AppLanguage.ARABIC.name,
            title = stringResource(R.string.arabic),
            icon = Icons.Rounded.Language,
            summary = stringResource(R.string.language_arabic_desc)
        ),
        SelectionOption(
            value = AppLanguage.ENGLISH.name,
            title = stringResource(R.string.english),
            icon = Icons.Rounded.Translate,
            summary = stringResource(R.string.language_english_desc)
        )
    )

    val items = listOf(
        SettingItem.Choice(
            key = "language",
            title = stringResource(R.string.language),
            summary = languageSummary,
            icon = languageIcon,
            enabled = true,
            autoSave = true,
            selectedOption = currentLanguage.name,
            options = languageOptions,
            onOptionSelected = { selectedValue ->
                runCatching { AppLanguage.valueOf(selectedValue) }.getOrNull()?.let { lang ->
                    onLanguageChanged(lang)
                }
            }
        )
    )

    return SettingSection(
        title = stringResource(R.string.language),
        icon = Icons.Rounded.Language,
        items = items
    )
}
