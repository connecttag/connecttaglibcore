package org.connecttag.lib.kotlin.core.aboutapp.compose

import androidx.compose.runtime.Composable
import org.connecttag.lib.kotlin.core.aboutapp.AboutAppProfile
import org.connecttag.lib.kotlin.core.aboutapp.AboutAppSocialLink

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
    val website: AboutAppWebsite? = null,
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
    val socialIcon: @Composable (AboutAppSocialLink) -> Unit = { link ->
        // Default implementation will be provided in the dialog file
    },
)

fun AboutAppUiProperties.withDefaults(
    translate: (String) -> String = this.translate,
): AboutAppUiProperties {
    val finalName = profile.name ?: profile.nameKey?.let(translate) ?: name
    val finalDescription = profile.description ?: profile.descriptionKey?.let(translate) ?: description
    return copy(
        name = finalName,
        description = finalDescription,
        translate = translate,
        logoContentDescription = finalName.takeIf { it.isNotBlank() },
    )
}
