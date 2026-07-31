package org.connecttag.lib.kotlin.core.aboutapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.connecttag.lib.kotlin.core.R
import org.connecttag.lib.kotlin.core.aboutapp.compose.AboutAppPresentation
import org.connecttag.lib.kotlin.core.settings.SettingsDuotoneIcon

@Composable
fun ConnectTagInfoScreen(
    onBack: () -> Unit,
    onWebsiteClick: (String) -> Unit,
    onSocialLinkClick: (AboutAppSocialLink) -> Unit,
) {
    val profile = AboutAppProfile(
        id = "connecttag",
        name = "كونكت تاق للخدمات والمستلزمات التقنية والتسويق الرقمي",
        description = "نقدم حلولاً تقنية متكاملة ومستلزمات فنية متميزة بالإضافة إلى خدمات التسويق الرقمي المبتكرة.",
        socialLinks = listOf(
            AboutData(SocialNetworkPlatform.Telegram, "connecttagye").toAboutAppSocialLink(),
            AboutData(SocialNetworkPlatform.Youtube, "connecttagye").toAboutAppSocialLink(),
            AboutData(SocialNetworkPlatform.Instagram, "connecttagye").toAboutAppSocialLink(),
            AboutData(SocialNetworkPlatform.Facebook, "connecttagye").toAboutAppSocialLink(),
            AboutData(SocialNetworkPlatform.X, "connecttagye").toAboutAppSocialLink(),
            AboutData(SocialNetworkPlatform.LinkedIn, "connecttagye").toAboutAppSocialLink(),
            AboutData(SocialNetworkPlatform.Pinterest, "connecttagye").toAboutAppSocialLink(),
            AboutData(SocialNetworkPlatform.Gmail, "info@connecttag.org").toAboutAppSocialLink(),
            AboutData(SocialNetworkPlatform.GooglePlay, "6507822077747485835").toAboutAppSocialLink(),
        )
    )

    val properties = AboutAppUiProperties(
        profile = profile,
        title = "عن المطور",
        labels = AboutAppDialogLabels(
            infoContentDescription = stringResource(R.string.about_app_info_desc),
            closeContentDescription = stringResource(R.string.about_app_close_desc),
            websiteTitle = stringResource(R.string.about_app_website)
        ),
        websites = listOf(
            AboutAppWebsite("الموقع الرسمي (1)", "https://connecttag.org"),
            AboutAppWebsite("الموقع الرسمي (2)", "https://connecttagye.com")
        ),
        repositories = listOf(
            AboutAppRepository("GitHub", "https://github.com/connecttagye"),
            AboutAppRepository("GitLab", "https://gitlab.com/connecttagye")
        ),
        options = AboutAppPresentationOptions(
            mode = AboutAppPresentationMode.FullScreen,
            showCloseButton = false
        ),
        onWebsiteClick = onWebsiteClick,
        onSocialLinkClick = onSocialLinkClick,
        onDismissRequest = onBack,
        logo = {
            SettingsDuotoneIcon(
                imageVector = Icons.Default.Business,
                contentDescription = null,
                containerSize = 80.dp,
                iconSize = 40.dp
            )
        }
    )

    AboutAppPresentation(properties = properties)
}
