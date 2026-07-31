package org.connecttag.lib.kotlin.core.aboutapp.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.connecttag.lib.kotlin.core.R
import org.connecttag.lib.kotlin.core.aboutapp.*
import org.connecttag.lib.kotlin.core.settings.SettingsDuotoneIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppPresentation(
    properties: AboutAppUiProperties,
    modifier: Modifier = Modifier,
) {
    val name = properties.profile.name ?: properties.name
    val description = properties.profile.description ?: properties.description
    val finalProperties = properties.copy(name = name, description = description)

    val content: @Composable (Modifier, Boolean) -> Unit = { contentModifier, isFullScreen ->
        AboutAppContent(
            properties = finalProperties,
            modifier = contentModifier,
            showCloseButton = finalProperties.options.showCloseButton && !isFullScreen,
        )
    }

    when (finalProperties.options.mode) {
        AboutAppPresentationMode.Dialog -> Dialog(
            onDismissRequest = finalProperties.onDismissRequest,
            properties = DialogProperties(
                dismissOnBackPress = finalProperties.options.dismissOnBack,
                dismissOnClickOutside = true,
            ),
        ) {
            Surface(
                modifier = modifier.widthIn(max = finalProperties.options.maxDialogWidthDp.coerceAtLeast(1).dp),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 4.dp,
            ) {
                content(Modifier, false)
            }
        }
        AboutAppPresentationMode.BottomSheet -> ModalBottomSheet(
            onDismissRequest = finalProperties.onDismissRequest,
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
            dragHandle = if (finalProperties.options.showDragHandle) {
                { BottomSheetDefaults.DragHandle() }
            } else {
                null
            },
        ) {
            content(
                modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                false,
            )
        }
        AboutAppPresentationMode.FullScreen -> Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(finalProperties.title) },
                    navigationIcon = {
                        IconButton(onClick = finalProperties.onDismissRequest) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = finalProperties.labels.closeContentDescription,
                            )
                        }
                    },
                )
            },
        ) { paddingValues ->
            content(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .navigationBarsPadding(),
                true,
            )
        }
    }
}

@Composable
fun AboutAppPresentation(
    profile: AboutAppProfile,
    title: String,
    labels: AboutAppDialogLabels,
    onDismissRequest: () -> Unit,
    onWebsiteClick: (String) -> Unit,
    onSocialLinkClick: (AboutAppSocialLink) -> Unit,
    modifier: Modifier = Modifier,
    presentationMode: AboutAppPresentationMode = AboutAppPresentationMode.Dialog,
    options: AboutAppPresentationOptions = AboutAppPresentationOptions(mode = presentationMode),
    translate: (String) -> String = { it },
    name: String = profile.name ?: profile.nameKey.orEmpty(),
    description: String? = profile.description ?: profile.descriptionKey,
    versionText: String? = null,
    websites: List<AboutAppWebsite> = emptyList(),
    repositories: List<AboutAppRepository> = emptyList(),
    socialLinks: List<AboutAppSocialLink> = profile.socialLinks,
    logoContentDescription: String? = name.takeIf { it.isNotBlank() },
    logo: @Composable (contentDescription: String?) -> Unit = {},
    cover: @Composable () -> Unit = {},
    socialIcon: @Composable (AboutAppSocialLink) -> Unit = { link ->
        DefaultSocialIcon(
            contentDescription = link.label ?: link.network.displayName,
            iconKey = link.network.iconKey,
        )
    },
) {
    AboutAppPresentation(
        properties = AboutAppUiProperties(
            profile = profile,
            title = title,
            labels = labels,
            name = name,
            description = description,
            versionText = versionText,
            websites = websites,
            repositories = repositories,
            socialLinks = socialLinks,
            options = options,
            translate = translate,
            logoContentDescription = logoContentDescription,
            onWebsiteClick = onWebsiteClick,
            onSocialLinkClick = onSocialLinkClick,
            onDismissRequest = onDismissRequest,
            logo = logo,
            cover = cover,
            socialIcon = socialIcon,
        ),
        modifier = modifier,
    )
}

@Composable
fun AboutAppDialog(
    profile: AboutAppProfile,
    title: String,
    labels: AboutAppDialogLabels,
    onDismissRequest: () -> Unit,
    onWebsiteClick: (String) -> Unit,
    onSocialLinkClick: (AboutAppSocialLink) -> Unit,
    modifier: Modifier = Modifier,
    translate: (String) -> String = { it },
    name: String = profile.name ?: profile.nameKey.orEmpty(),
    description: String? = profile.description ?: profile.descriptionKey,
    versionText: String? = null,
    websites: List<AboutAppWebsite> = emptyList(),
    repositories: List<AboutAppRepository> = emptyList(),
    socialLinks: List<AboutAppSocialLink> = profile.socialLinks,
    options: AboutAppPresentationOptions = AboutAppPresentationOptions(),
    logoContentDescription: String? = name.takeIf { it.isNotBlank() },
    logo: @Composable (contentDescription: String?) -> Unit = {},
    cover: @Composable () -> Unit = {},
    socialIcon: @Composable (AboutAppSocialLink) -> Unit = { link ->
        DefaultSocialIcon(
            contentDescription = link.label ?: link.network.displayName,
            iconKey = link.network.iconKey,
        )
    },
) {
    AboutAppPresentation(
        profile = profile,
        title = title,
        labels = labels,
        onDismissRequest = onDismissRequest,
        onWebsiteClick = onWebsiteClick,
        onSocialLinkClick = onSocialLinkClick,
        modifier = modifier,
        options = options.copy(mode = AboutAppPresentationMode.Dialog),
        translate = translate,
        name = name,
        description = description,
        versionText = versionText,
        websites = websites,
        repositories = repositories,
        socialLinks = socialLinks,
        logoContentDescription = logoContentDescription,
        logo = logo,
        cover = cover,
        socialIcon = socialIcon,
    )
}

@Composable
fun AboutAppContent(
    properties: AboutAppUiProperties,
    modifier: Modifier = Modifier,
    showCloseButton: Boolean = properties.options.showCloseButton,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        properties.cover()

        if (showCloseButton) {
            AboutAppHeader(
                title = properties.title,
                labels = properties.labels,
                onDismissRequest = properties.onDismissRequest,
            )
        }
        properties.logo(properties.logoContentDescription)
        Text(
            text = properties.name,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 8.dp),
        )
        if (!properties.description.isNullOrBlank()) {
            Text(
                text = properties.description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 12.dp),
            )
        } else {
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (!properties.versionText.isNullOrBlank()) {
            Text(
                text = properties.versionText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        properties.websites.forEach { website ->
            WebsiteRow(
                website = website,
                labels = properties.labels,
                onWebsiteClick = properties.onWebsiteClick,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        properties.repositories.forEach { repo ->
            RepositoryRow(
                repo = repo,
                onRepoClick = properties.onWebsiteClick,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        MetadataSection(
            metadata = properties.profile.metadata,
            translate = properties.translate,
        )

        SocialLinksRow(
            socialLinks = properties.socialLinks.filter { it.active },
            onSocialLinkClick = properties.onSocialLinkClick,
            onSocialLinkLongClick = properties.onSocialLinkLongClick,
            socialIcon = properties.socialIcon,
            spacingDp = properties.options.socialSpacingDp,
        )
    }
}

@Composable
private fun MetadataSection(
    metadata: Map<String, String>,
    translate: (String) -> String,
) {
    if (metadata.isEmpty()) return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
        metadata.forEach { (key, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = translate(key),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun AboutAppHeader(
    title: String,
    labels: AboutAppDialogLabels,
    onDismissRequest: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = labels.infoContentDescription,
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onDismissRequest) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = labels.closeContentDescription,
                tint = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun WebsiteRow(
    website: AboutAppWebsite,
    labels: AboutAppDialogLabels,
    onWebsiteClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClickLabel = labels.websiteTitle,
                role = Role.Button,
            ) {
                onWebsiteClick(website.url)
            }
            .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Language,
            contentDescription = labels.websiteTitle,
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = website.label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = website.url,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun RepositoryRow(
    repo: AboutAppRepository,
    onRepoClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button) {
                onRepoClick(repo.url)
            }
            .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Link,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = repo.label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = repo.url,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SocialLinksRow(
    socialLinks: List<AboutAppSocialLink>,
    onSocialLinkClick: (AboutAppSocialLink) -> Unit,
    onSocialLinkLongClick: (AboutAppSocialLink) -> Unit,
    socialIcon: @Composable (AboutAppSocialLink) -> Unit,
    spacingDp: Int = 8,
) {
    if (socialLinks.isEmpty()) return
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(spacingDp.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(spacingDp.dp),
    ) {
        socialLinks.forEach { link ->
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .combinedClickable(
                        onClick = { onSocialLinkClick(link) },
                        onLongClick = { onSocialLinkLongClick(link) }
                    )
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                socialIcon(link)
            }
        }
    }
}

@Composable
private fun DefaultSocialIcon(
    contentDescription: String,
    iconKey: String? = null,
) {
    val iconRes = resolveSocialIconResource(iconKey)
    if (iconRes != null) {
        SettingsDuotoneIcon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.primary,
            containerSize = 44.dp,
            iconSize = 24.dp,
        )
    } else {
        SettingsDuotoneIcon(
            imageVector = Icons.Default.Link,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.primary,
            containerSize = 44.dp,
            iconSize = 24.dp,
        )
    }
}

private fun resolveSocialIconResource(iconKey: String?): Int? {
    return when (iconKey?.lowercase()) {
        "facebook" -> R.drawable.ic_facebook
        "whatsapp", "whatsappchannel", "whatsappgroup" -> R.drawable.ic_whatsapp
        "instagram" -> R.drawable.ic_instagram
        "telegram" -> R.drawable.ic_telegram
        "x", "twitter" -> R.drawable.ic_x
        "mail", "gmail", "email" -> R.drawable.ic_mail
        "youtube" -> R.drawable.ic_youtube
        "linkedin" -> R.drawable.ic_linkedin
        "tiktok" -> R.drawable.ic_tiktok
        "threads" -> R.drawable.ic_threads
        "googleplay", "playstore", "play" -> R.drawable.ic_google_play
        "pinterest", "github", "gitlab" -> R.drawable.ic_website
        else -> null
    }
}
