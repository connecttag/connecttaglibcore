package org.connecttag.lib.kotlin.core.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.connecttag.lib.kotlin.core.R

@Composable
fun supportSettingsSection(
    onOpenAboutApp: () -> Unit,
    onOpenAboutDeveloper: () -> Unit,
    onOpenPrivacyPolicy: (() -> Unit)? = null,
): SettingSection {
    val items = buildList {
        add(
            SettingItem.Action(
                key = "about_app",
                title = stringResource(R.string.support_about_app_title),
                summary = stringResource(R.string.support_about_app_desc),
                icon = Icons.Outlined.Info,
                onClick = onOpenAboutApp
            )
        )
        add(
            SettingItem.Action(
                key = "about_developer",
                title = stringResource(R.string.support_about_developer_title),
                summary = stringResource(R.string.support_about_developer_desc),
                icon = Icons.Outlined.Person,
                onClick = onOpenAboutDeveloper
            )
        )
        onOpenPrivacyPolicy?.let {
            add(
                SettingItem.Action(
                    key = "privacy_policy",
                    title = stringResource(R.string.support_privacy_policy_title),
                    summary = stringResource(R.string.support_privacy_policy_desc),
                    icon = Icons.Outlined.PrivacyTip,
                    onClick = it
                )
            )
        }
    }

    return SettingSection(
        title = stringResource(R.string.support_settings_title),
        description = null,
        icon = Icons.Outlined.Info,
        items = items
    )
}
