package org.connecttag.lib.kotlin.core.aboutapp

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.connecttag.lib.kotlin.core.R
import org.connecttag.lib.kotlin.core.aboutapp.compose.resolveSocialIconResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BaseAboutAppScreen(
    appName: String,
    versionText: String,
    description: String,
    appIcon: Any, // Painter, ImageVector, Resource ID (Int), or URL/Model
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.about_app),
    packageName: String? = null,
    coverContent: @Composable () -> Unit = { DefaultAboutCover() },
    actions: List<AboutAppRowAction> = emptyList(),
    websites: List<AboutAppWebsite> = emptyList(),
    socialLinks: List<AboutAppSocialLinkData> = emptyList(),
    developerAction: (() -> Unit)? = null,
    footerContent: (@Composable () -> Unit)? = null,
    onUrlClick: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { /* Empty title to avoid redundancy with prominently displayed appName */ },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Header Section
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    coverContent()
                    
                    Surface(
                        modifier = Modifier
                            .size(90.dp)
                            .align(Alignment.BottomCenter)
                            .offset(y = (-10).dp),
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        shadowElevation = 6.dp,
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    ) {
                        AboutAppIcon(model = appIcon, modifier = Modifier.padding(16.dp))
                    }
                }
            }

            // App Name & Version
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = appName,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (versionText.isNotBlank()) {
                        Surface(
                            modifier = Modifier.padding(top = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = versionText,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (!packageName.isNullOrBlank()) {
                        Text(
                            text = packageName,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Description
            if (description.isNotBlank()) {
                item {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 26.sp,
                        modifier = Modifier.padding(horizontal = 24.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Websites Section
            if (websites.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            text = stringResource(R.string.about_app_website),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        ElevatedCard(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column {
                                websites.forEachIndexed { index, website ->
                                    AboutActionRow(
                                        title = website.label,
                                        icon = Icons.Default.Language,
                                        onClick = { onUrlClick(website.url) }
                                    )
                                    if (index < websites.size - 1) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 20.dp),
                                            thickness = 0.5.dp,
                                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Actions Card
            if (actions.isNotEmpty() || developerAction != null) {
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            text = stringResource(R.string.support_settings_title),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        ElevatedCard(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column {
                                actions.forEachIndexed { index, action ->
                                    AboutActionRow(
                                        title = action.title,
                                        icon = action.icon,
                                        onClick = {
                                            if (action.onClick != null) {
                                                action.onClick.invoke()
                                            } else if (!action.url.isNullOrBlank()) {
                                                onUrlClick(action.url)
                                            }
                                        }
                                    )
                                    if (index < actions.size - 1 || developerAction != null) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 20.dp),
                                            thickness = 0.5.dp,
                                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                                
                                developerAction?.let {
                                    AboutActionRow(
                                        title = stringResource(R.string.about_developer_title),
                                        icon = Icons.Default.Code,
                                        onClick = it
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Social Section
            if (socialLinks.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                       
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            socialLinks.forEach { link ->
                                ReusableSocialIcon(
                                    iconRes = link.iconRes,
                                    tint = link.tint,
                                    onClick = { onUrlClick(link.url) }
                                )
                            }
                        }
                    }
                }
            }

            // Optional Footer Content Slot
            if (footerContent != null) {
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        footerContent()
                    }
                }
            }
        }
    }
}

/**
 * Overload that uses [AboutAppProfile] for easier integration.
 */
@Composable
fun BaseAboutAppScreen(
    profile: AboutAppProfile,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    versionText: String = "",
    appIcon: Any = R.drawable.connect_tag_logo, // Fallback to logo
    onUrlClick: (String) -> Unit = {},
    developerAction: (() -> Unit)? = null,
    extraActions: List<AboutAppRowAction> = emptyList(),
    translate: (String?) -> String = { it ?: "" }
) {
    // Bridge AboutAppProfile to BaseAboutAppScreen parameters
    BaseAboutAppScreen(
        appName = translate(profile.name ?: profile.nameKey),
        versionText = versionText,
        description = translate(profile.description ?: profile.descriptionKey),
        appIcon = profile.logo ?: appIcon,
        onBack = onBack,
        modifier = modifier,
        actions = extraActions,
        websites = profile.socialLinks.filter { it.network.kind == SocialLinkKind.Url }
            .map { AboutAppWebsite(it.label ?: it.network.displayName, it.network.webUrlPrefix + it.value) },
        socialLinks = profile.socialLinks.mapNotNull { link ->
            val iconRes = resolveSocialIconResource(link.network.iconKey)
            iconRes?.let {
                AboutAppSocialLinkData(
                    iconRes = it,
                    url = if (link.network.kind == SocialLinkKind.Email) "mailto:${link.value}" else link.network.webUrlPrefix + link.value,
                    tint = if (link.network.iconKey.contains("color")) Color.Unspecified else null
                )
            }
        },
        developerAction = developerAction,
        onUrlClick = onUrlClick
    )
}

@Composable
private fun DefaultAboutCover() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    )
}

@Composable
private fun AboutAppIcon(model: Any, modifier: Modifier = Modifier) {
    when (model) {
        is Painter -> Image(painter = model, contentDescription = null, modifier = modifier)
        is ImageVector -> Image(imageVector = model, contentDescription = null, modifier = modifier)
        else -> {
            // Use AsyncImage for everything else (Int Resource IDs, URLs, etc.)
            // to support Adaptive Icons and other non-standard drawables
            AsyncImage(
                model = model,
                contentDescription = null,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun AboutActionRow(
    title: String,
    icon: Any, // ImageVector, Painter, or Int (DrawableRes ID)
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(42.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ) {
            when (icon) {
                is ImageVector -> Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(10.dp)
                )
                is Painter -> Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(10.dp)
                )
                is Int -> AsyncImage(
                    model = icon,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.padding(10.dp)
                )
                else -> {
                    AsyncImage(
                        model = icon,
                        contentDescription = null,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
private fun ReusableSocialIcon(
    iconRes: Int,
    tint: Color? = null,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
        modifier = Modifier.size(52.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            AsyncImage(
                model = iconRes,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                colorFilter = tint?.let { ColorFilter.tint(it) }
                    ?: ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

data class AboutAppRowAction(
    val title: String,
    val icon: Any, // ImageVector, Painter, or Int
    val url: String? = null,
    val onClick: (() -> Unit)? = null
) {
    constructor(title: String, icon: Any, url: String) : this(title = title, icon = icon, url = url, onClick = null)
    constructor(title: String, icon: Any, onClick: () -> Unit) : this(title = title, icon = icon, url = null, onClick = onClick)
}

data class AboutAppSocialLinkData(
    val iconRes: Int,
    val url: String,
    val tint: Color? = null
)
