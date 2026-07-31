package org.connecttag.lib.kotlin.core.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.connecttag.lib.kotlin.core.R

/**
 * Provides a standard "Support & About" section for settings screens.
 */
@Composable
fun provideSupportAboutSection(
    onShareApp: () -> Unit,
    onContactSupport: () -> Unit,
    onPrivacyPolicy: () -> Unit,
    onTermsOfService: () -> Unit,
    onAboutApp: () -> Unit,
    versionName: String? = null
): SettingSection {
    return SettingSection(
        title = stringResource(R.string.support_about),
        icon = Icons.AutoMirrored.Outlined.HelpOutline,
        items = listOf(
            SettingItem.Clickable(
                key = "share_app",
                title = stringResource(R.string.share_app),
                icon = Icons.Outlined.Share,
                onClick = onShareApp
            ),
            SettingItem.Clickable(
                key = "contact_support",
                title = stringResource(R.string.contact_support),
                icon = Icons.Outlined.Email,
                onClick = onContactSupport
            ),
            SettingItem.Clickable(
                key = "privacy_policy",
                title = stringResource(R.string.privacy_policy),
                icon = Icons.Outlined.PrivacyTip,
                onClick = onPrivacyPolicy
            ),
            SettingItem.Clickable(
                key = "terms_of_service",
                title = stringResource(R.string.terms_of_service),
                icon = Icons.Outlined.Description,
                onClick = onTermsOfService
            ),
            SettingItem.Clickable(
                key = "about_app",
                title = stringResource(R.string.about_app),
                summary = versionName?.let { "${stringResource(R.string.version)} $it" },
                icon = Icons.Outlined.Info,
                onClick = onAboutApp
            )
        )
    )
}
