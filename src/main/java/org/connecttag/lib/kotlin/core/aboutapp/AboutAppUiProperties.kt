package org.connecttag.lib.kotlin.core.aboutapp

import androidx.compose.runtime.Composable

/**
 * Properties for rendering the About App UI.
 * This groups parameters to avoid complex function signatures.
 */
data class AboutAppUiProperties(
    val profile: AboutAppProfile,
    val title: String,
    val labels: AboutAppDialogLabels,
    val name: String = profile.name.orEmpty(),
    val description: String? = profile.description,
    val versionText: String? = null,
    val websites: List<AboutAppWebsite> = emptyList(),
    val repositories: List<AboutAppRepository> = emptyList(),
    val socialLinks: List<AboutAppSocialLink> = profile.socialLinks,
    val options: AboutAppPresentationOptions = AboutAppPresentationOptions(),
    val translate: (String) -> String = { it },
    val logoContentDescription: String? = name.takeIf { it.isNotBlank() },
    val onWebsiteClick: (String) -> Unit = {},
    val onSocialLinkClick: (AboutAppSocialLink) -> Unit = {},
    val onSocialLinkLongClick: (AboutAppSocialLink) -> Unit = {},
    val onDismissRequest: () -> Unit = {},
    val logo: @Composable (contentDescription: String?) -> Unit = {},
    val cover: @Composable () -> Unit = {},
    val socialIcon: @Composable (AboutAppSocialLink) -> Unit = {},
)
