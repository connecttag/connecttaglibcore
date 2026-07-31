package org.connecttag.lib.kotlin.core.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents the base of any setting item.
 */
sealed class SettingItem {
    abstract val key: String
    abstract val title: String
    abstract val summary: String?
    abstract val icon: ImageVector?
    abstract val enabled: Boolean

    /** A clickable setting that triggers an action or navigation. */
    data class Clickable(
        override val key: String,
        override val title: String,
        override val summary: String? = null,
        override val icon: ImageVector? = null,
        val iconTargetState: ImageVector? = null,
        override val enabled: Boolean = true,
        val trailingContent: (@Composable () -> Unit)? = null,
        val onClick: () -> Unit
    ) : SettingItem()

    /** A setting with a switch/toggle. */
    data class Toggle(
        override val key: String,
        override val title: String,
        override val summary: String? = null,
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        val checked: Boolean,
        val trailingContent: (@Composable () -> Unit)? = null,
        val onCheckedChange: (Boolean) -> Unit
    ) : SettingItem()

    /** A setting that allows choosing from multiple options. */
    data class Choice(
        override val key: String,
        override val title: String,
        override val summary: String? = null,
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        val selectedOption: String,
        val options: List<SelectionOption>,
        val onOptionSelected: (String) -> Unit
    ) : SettingItem()

    /** A fully custom setting item. */
    data class Custom(
        override val key: String,
        override val title: String = "",
        override val summary: String? = null,
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        val content: @Composable () -> Unit
    ) : SettingItem()

    /** Legacy Action kept for backward compatibility if needed, but Clickable is preferred. */
    data class Action(
        override val key: String,
        override val title: String,
        override val summary: String? = null,
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        val onClick: () -> Unit
    ) : SettingItem()
}

/**
 * Represents a selectable option in a [SettingItem.Choice].
 */
data class SelectionOption(
    val value: String,
    val title: String,
    val icon: ImageVector? = null,
    val summary: String? = null
)

/**
 * Groups multiple [SettingItem]s together under a title.
 */
data class SettingSection(
    val title: String,
    val description: String? = null,
    val icon: ImageVector? = null,
    val items: List<SettingItem>
)
