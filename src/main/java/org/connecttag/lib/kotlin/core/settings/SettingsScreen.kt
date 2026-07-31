package org.connecttag.lib.kotlin.core.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.connecttag.lib.kotlin.core.R
import org.connecttag.lib.kotlin.core.theme.AppLanguage
import org.connecttag.lib.kotlin.core.theme.ThemeMode
import org.connecttag.lib.kotlin.core.theme.ThemeSettings
import org.connecttag.lib.kotlin.core.theme.engine.ConnectTagTheme

/**
 * Enhanced Settings Screen using modular components.
 */
@Composable
fun SettingsScreen(
    extraSections: (List<SettingSection>) -> List<SettingSection> = { it },
    extraItems: (LazyListScope.() -> Unit)? = null
) {
    val themeModeOptions = listOf(
        SelectionOption(ThemeMode.LIGHT.name, stringResource(R.string.light)),
        SelectionOption(ThemeMode.DARK.name, stringResource(R.string.dark)),
        SelectionOption(ThemeMode.SYSTEM.name, stringResource(R.string.system_default))
    )

    val languageOptions = listOf(
        SelectionOption(AppLanguage.ENGLISH.value, stringResource(R.string.english)),
        SelectionOption(AppLanguage.ARABIC.value, stringResource(R.string.arabic)),
        SelectionOption(AppLanguage.SYSTEM.value, stringResource(R.string.system_default))
    )

    val coreSections = listOf(
        SettingSection(
            title = stringResource(R.string.language),
            icon = Icons.Outlined.Language,
            items = listOf(
                SettingItem.Choice(
                    key = "app_language",
                    title = stringResource(R.string.language),
                    selectedOption = ThemeSettings.appLanguage.value,
                    options = languageOptions,
                    onOptionSelected = { ThemeSettings.appLanguage = AppLanguage.fromValue(it) }
                )
            )
        ),
        SettingSection(
            title = stringResource(R.string.appearance),
            icon = Icons.Outlined.ColorLens,
            items = listOf(
                SettingItem.Choice(
                    key = "theme_mode",
                    title = stringResource(R.string.appearance),
                    selectedOption = ThemeSettings.themeMode.name,
                    options = themeModeOptions,
                    onOptionSelected = { ThemeSettings.themeMode = ThemeMode.valueOf(it) }
                ),
                SettingItem.Toggle(
                    key = "dynamic_colors",
                    title = stringResource(R.string.dynamic_colors),
                    summary = stringResource(R.string.follow_system_colors),
                    checked = ThemeSettings.isDynamicColorEnabled,
                    onCheckedChange = { ThemeSettings.isDynamicColorEnabled = it }
                )
            )
        )
    )

    val allSections = extraSections(coreSections)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ConnectTagTheme.gradients.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Text(
                stringResource(R.string.settings),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        allSections.forEach { section ->
            item {
                RenderSettingSection(section)
            }
        }

        extraItems?.invoke(this)

        item {
            OutlinedButton(
                onClick = { ThemeSettings.resetToDefaults() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Rounded.RestartAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.reset_to_defaults))
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
