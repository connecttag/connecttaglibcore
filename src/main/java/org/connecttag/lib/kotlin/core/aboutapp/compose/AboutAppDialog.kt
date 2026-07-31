package org.connecttag.lib.kotlin.core.aboutapp.compose

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.Rule
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.connecttag.lib.kotlin.core.R
import org.connecttag.lib.kotlin.core.aboutapp.AboutAppProfile
import org.connecttag.lib.kotlin.core.aboutapp.AboutAppSocialLink
import org.connecttag.lib.kotlin.core.aboutapp.AppStoreSpec
import org.connecttag.lib.kotlin.core.settings.SettingsDuotoneIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppPresentation(
    properties: AboutAppUiProperties,
    modifier: Modifier = Modifier,
) {
    val translate = properties.translate
    val name = properties.profile.name ?: properties.profile.nameKey?.let(translate) ?: properties.name
    val description = properties.profile.description ?: properties.profile.descriptionKey?.let(translate) ?: properties.description
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
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

/** Legacy support and simple builder for AboutAppPresentation. */
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
    name: String = profile.name ?: profile.nameKey?.let(translate).orEmpty(),
    description: String? = profile.description ?: profile.descriptionKey?.let(translate),
    versionText: String? = null,
    website: AboutAppWebsite? = null,
    socialLinks: List<AboutAppSocialLink> = profile.socialLinks,
    logoContentDescription: String? = name.takeIf { it.isNotBlank() },
    logo: @Composable (contentDescription: String?) -> Unit = {},
    cover: @Composable () -> Unit = {},
    socialIcon: @Composable (AboutAppSocialLink) -> Unit = { link ->
        DefaultSocialIcon(
            contentDescription = link.label ?: translate(link.network.displayName),
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
            website = website,
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
    name: String = profile.name ?: profile.nameKey?.let(translate).orEmpty(),
    description: String? = profile.description ?: profile.descriptionKey?.let(translate),
    versionText: String? = null,
    website: AboutAppWebsite? = null,
    socialLinks: List<AboutAppSocialLink> = profile.socialLinks,
    options: AboutAppPresentationOptions = AboutAppPresentationOptions(),
    logoContentDescription: String? = name.takeIf { it.isNotBlank() },
    logo: @Composable (contentDescription: String?) -> Unit = {},
    cover: @Composable () -> Unit = {},
    socialIcon: @Composable (AboutAppSocialLink) -> Unit = { link ->
        DefaultSocialIcon(
            contentDescription = link.label ?: translate(link.network.displayName),
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
        website = website,
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
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        properties.cover()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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

            // Address and Phones
            if (!properties.profile.address.isNullOrBlank() || properties.profile.phoneNumbers.isNotEmpty()) {
                InfoSection(
                    address = properties.profile.address,
                    phones = properties.profile.phoneNumbers
                )
            }

            if (properties.website != null && (properties.website.url.isNotBlank())) {
                WebsiteRow(
                    website = properties.website,
                    labels = properties.labels,
                    onWebsiteClick = properties.onWebsiteClick,
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Stores Section
            if (properties.profile.stores.isNotEmpty()) {
                StoresSection(
                    stores = properties.profile.stores,
                    labels = properties.labels,
                    onUrlClick = properties.onWebsiteClick
                )
            }

            // Policies Section
            if (!properties.profile.privacyPolicyUrl.isNullOrBlank() || !properties.profile.termsOfUseUrl.isNullOrBlank()) {
                PoliciesSection(
                    privacyUrl = properties.profile.privacyPolicyUrl,
                    termsUrl = properties.profile.termsOfUseUrl,
                    labels = properties.labels,
                    onUrlClick = properties.onWebsiteClick
                )
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
}

@Composable
private fun InfoSection(
    address: String?,
    phones: List<String>
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        address?.let {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = it, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
            }
        }
        phones.forEach { phone ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = phone, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StoresSection(
    stores: List<AppStoreSpec>,
    labels: AboutAppDialogLabels,
    onUrlClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = labels.storesTitle ?: "Available on",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            stores.forEach { store ->
                AssistChip(
                    onClick = { onUrlClick(store.url) },
                    label = { Text(store.label ?: store.type) },
                    leadingIcon = { Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
            }
        }
    }
}

@Composable
private fun PoliciesSection(
    privacyUrl: String?,
    termsUrl: String?,
    labels: AboutAppDialogLabels,
    onUrlClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        privacyUrl?.let {
            TextButton(onClick = { onUrlClick(it) }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.Policy, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = labels.privacyPolicyTitle ?: "Privacy Policy", style = MaterialTheme.typography.labelMedium)
            }
        }
        termsUrl?.let {
            TextButton(onClick = { onUrlClick(it) }, modifier = Modifier.weight(1f)) {
                Icon(Icons.AutoMirrored.Filled.Rule, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = labels.termsOfUseTitle ?: "Terms of Use", style = MaterialTheme.typography.labelMedium)
            }
        }
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
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClickLabel = labels.websiteTitle,
                role = Role.Button,
            ) {
                onWebsiteClick(website.url)
            },
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SettingsDuotoneIcon(
                imageVector = Icons.Default.Language,
                contentDescription = labels.websiteTitle,
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = labels.websiteTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = website.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Visible,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SocialLinksRow(
    socialLinks: List<AboutAppSocialLink>,
    onSocialLinkClick: (AboutAppSocialLink) -> Unit,
    onSocialLinkLongClick: (AboutAppSocialLink) -> Unit = {},
    socialIcon: @Composable (AboutAppSocialLink) -> Unit,
    spacingDp: Int = 8,
) {
    if (socialLinks.isEmpty()) return
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(spacingDp.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(spacingDp.dp),
    ) {
        socialLinks.forEach { link ->
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .combinedClickable(
                        onClick = { onSocialLinkClick(link) },
                        onLongClick = { onSocialLinkLongClick(link) },
                        role = Role.Button,
                    ),
                contentAlignment = Alignment.Center,
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
        else -> null
    }
}
