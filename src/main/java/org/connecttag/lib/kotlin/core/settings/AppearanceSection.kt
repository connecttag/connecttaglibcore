package org.connecttag.lib.kotlin.core.settings

import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrightnessAuto
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.connecttag.lib.kotlin.core.R
import org.connecttag.lib.kotlin.core.theme.ThemeMode
import org.connecttag.lib.kotlin.core.theme.ThemeSettings

/**
 * Builds the Appearance (المظهر) setting section with Theme Mode and Dynamic Colors.
 *
 * @param themeMode Current selected ThemeMode (defaults to ThemeSettings.themeMode)
 * @param isDynamicColor Whether dynamic color is enabled (defaults to ThemeSettings.isDynamicColorEnabled)
 * @param onThemeModeChanged Callback when theme mode is selected
 * @param onDynamicColorChanged Callback when dynamic color toggle is changed
 */
@Composable
fun appearanceSettingsSection(
    themeMode: ThemeMode = ThemeSettings.themeMode,
    isDynamicColor: Boolean = ThemeSettings.isDynamicColorEnabled,
    onThemeModeChanged: (ThemeMode) -> Unit = { ThemeSettings.themeMode = it },
    onDynamicColorChanged: (Boolean) -> Unit = { 
        ThemeSettings.isDynamicColorEnabled = it
        if (it) {
            ThemeSettings.customSeedColor = null
        }
    }
): SettingSection {
    val themeSummary = when (themeMode) {
        ThemeMode.SYSTEM -> stringResource(R.string.system_default)
        ThemeMode.LIGHT -> stringResource(R.string.light)
        ThemeMode.DARK -> stringResource(R.string.dark)
    }

    val themeIcon = when (themeMode) {
        ThemeMode.SYSTEM -> Icons.Rounded.BrightnessAuto
        ThemeMode.LIGHT -> Icons.Rounded.LightMode
        ThemeMode.DARK -> Icons.Rounded.DarkMode
    }

    val themeOptions = listOf(
        SelectionOption(
            value = ThemeMode.SYSTEM.name,
            title = stringResource(R.string.system_default),
            icon = Icons.Rounded.BrightnessAuto,
            summary = stringResource(R.string.theme_system_desc)
        ),
        SelectionOption(
            value = ThemeMode.LIGHT.name,
            title = stringResource(R.string.light),
            icon = Icons.Rounded.LightMode,
            summary = stringResource(R.string.theme_light_desc)
        ),
        SelectionOption(
            value = ThemeMode.DARK.name,
            title = stringResource(R.string.dark),
            icon = Icons.Rounded.DarkMode,
            summary = stringResource(R.string.theme_dark_desc)
        )
    )

    val isDynamicColorSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val dynamicColorSummary = if (isDynamicColorSupported) {
        stringResource(R.string.follow_system_colors)
    } else {
        stringResource(R.string.dynamic_colors_unsupported)
    }

    val items = listOf(
        SettingItem.Choice(
            key = "theme_mode",
            title = stringResource(R.string.appearance),
            summary = themeSummary,
            icon = themeIcon,
            enabled = true,
            autoSave = true,
            selectedOption = themeMode.name,
            options = themeOptions,
            onOptionSelected = { selectedValue ->
                runCatching { ThemeMode.valueOf(selectedValue) }.getOrNull()?.let { mode ->
                    onThemeModeChanged(mode)
                }
            }
        ),
        SettingItem.Toggle(
            key = "dynamic_colors",
            title = stringResource(R.string.dynamic_colors),
            summary = dynamicColorSummary,
            icon = Icons.Rounded.Palette,
            enabled = isDynamicColorSupported,
            autoSave = true,
            checked = isDynamicColor && isDynamicColorSupported,
            onCheckedChange = { isChecked ->
                onDynamicColorChanged(isChecked)
            }
        )
    )

    return SettingSection(
        title = stringResource(R.string.appearance),
        icon = Icons.Rounded.Palette,
        items = items
    )
}
