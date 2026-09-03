package org.connecttag.lib.kotlin.core.aboutapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import org.connecttag.lib.kotlin.core.R
import org.connecttag.lib.kotlin.core.settings.SettingsDuotoneIcon

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ConnectTagInfoScreen(
    onBack: () -> Unit,
    onUrlClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { /* Empty title to avoid redundancy with content */ },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            ) {
                AsyncImage(
                    model = R.drawable.connect_tag_logo,
                    contentDescription = null,
                    modifier = Modifier.padding(16.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = stringResource(R.string.developer_name_full),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = stringResource(R.string.developer_description_full),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Website Section
            InfoRow(
                icon = Icons.Default.Language,
                label = stringResource(R.string.developer_website),
                value = "connecttag.org",
                onClick = { onUrlClick("https://connecttag.org") }
            )

            InfoRow(
                icon = Icons.Default.Email,
                label = stringResource(R.string.developer_email),
                value = "info@connecttag.org",
                onClick = { onUrlClick("mailto:info@connecttag.org") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.code_repositories),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            val repoLinks = listOf(
                R.drawable.github to "https://github.com/connecttagye",
                R.drawable.gitlab to "https://gitlab.com/connecttagye"
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
            ) {
                items(repoLinks) { (icon, url) ->
                    SocialIcon(iconRes = icon, label = "") { onUrlClick(url) }
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.contact_developer),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            val socialLinks = listOf(
                R.drawable.facebook to "https://www.facebook.com/connecttagye",
                R.drawable.telegram to "https://t.me/connecttagye",
                R.drawable.whatsapp to "https://whatsapp.com/channel/0029VaasCef8qIztDeS8Sp1R",
                R.drawable.x to "https://x.com/connecttagye",
                R.drawable.linkedin to "https://www.linkedin.com/in/connecttagye",
                R.drawable.instagram to "https://www.instagram.com/connecttagye",
                R.drawable.threads to "https://www.threads.net/@connecttagye",
                R.drawable.snapchat to "https://www.snapchat.com/add/connecttagye",
                R.drawable.tiktok to "https://www.tiktok.com/@connecttagye",
                R.drawable.youtube to "https://www.youtube.com/@connecttagye",
                R.drawable.kik to "http://kik.me/connecttagye",
                R.drawable.pinterest to "https://www.pinterest.com/connecttagye",
                R.drawable.reddit to "https://www.reddit.com/user/connecttagye",
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(socialLinks) { (icon, url) ->
                    SocialIcon(iconRes = icon, label = "") { onUrlClick(url) }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.Download_Our_Apps),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            val appLinks = listOf(
                R.drawable.telegram to "https://t.me/ConnectTagApps",
                R.drawable.google_play to "https://play.google.com/store/apps/dev?id=6507822077747485835"
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
            ) {
                items(appLinks) { (icon, url) ->
                    SocialIcon(iconRes = icon, label = "") { onUrlClick(url) }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(value, style = MaterialTheme.typography.bodyLarge, overflow = TextOverflow.Ellipsis, maxLines = 1)
            }
        }
    }
}

@Composable
private fun SocialIcon(
    iconRes: Int,
    label: String,
    tint: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = tint.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, tint.copy(alpha = 0.2f)),
        modifier = Modifier.size(56.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            AsyncImage(
                model = iconRes,
                contentDescription = label,
                modifier = Modifier.size(28.dp),
                colorFilter = if (tint != Color.Unspecified) ColorFilter.tint(tint) else null
            )
        }
    }
}
