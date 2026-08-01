package org.connecttag.lib.kotlin.core.aboutapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
                title = { Text("عن المطور") },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingsDuotoneIcon(
                imageVector = Icons.Default.Business,
                contentDescription = null,
                containerSize = 100.dp,
                iconSize = 50.dp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "كونكت تاق للخدمات والمستلزمات التقنية والتسويق الرقمي",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "نقدم حلولاً تقنية متكاملة ومستلزمات فنية متميزة بالإضافة إلى خدمات التسويق الرقمي المبتكرة.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Website Section
            InfoRow(
                icon = Icons.Default.Language,
                label = "موقعنا الالكتروني",
                value = "connecttag.org",
                onClick = { onUrlClick("https://connecttag.org") }
            )

            InfoRow(
                icon = Icons.Default.Email,
                label = "البريد الإلكتروني",
                value = "info@connecttag.org",
                onClick = { onUrlClick("mailto:info@connecttag.org") }
            )

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "حساباتنا على مستودعات الأكواد",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(12.dp))
            InfoRow(
                icon = Icons.Default.Link,
                label = "GitHub",
                value = "github.com/connecttagye",
                onClick = { onUrlClick("https://github.com/connecttagye") }
            )
            InfoRow(
                icon = Icons.Default.Link,
                label = "GitLab",
                value = "gitlab.com/connecttagye",
                onClick = { onUrlClick("https://gitlab.com/connecttagye") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "تواصل معنا",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SocialIcon(R.drawable.ic_telegram, "Telegram") { onUrlClick("https://t.me/connecttagye") }
                SocialIcon(R.drawable.ic_youtube, "YouTube") { onUrlClick("https://www.youtube.com/connecttagye") }
                SocialIcon(R.drawable.ic_instagram, "Instagram") { onUrlClick("https://www.instagram.com/connecttagye/") }
                SocialIcon(R.drawable.ic_facebook, "Facebook") { onUrlClick("https://www.facebook.com/connecttagye") }
                SocialIcon(R.drawable.ic_x, "X") { onUrlClick("https://twitter.com/connecttagye") }
                SocialIcon(R.drawable.ic_linkedin, "LinkedIn") { onUrlClick("https://www.linkedin.com/in/connecttagye") }
                SocialIcon(R.drawable.ic_website, "Pinterest") { onUrlClick("https://www.pinterest.com/connecttagye/") }
                SocialIcon(R.drawable.ic_playstore, "Play Store") { onUrlClick("https://play.google.com/store/apps/dev?id=6507822077747485835") }
                SocialIcon(R.drawable.ic_telegram, "Apps Channel") { onUrlClick("https://t.me/ConnectTagApps") }
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
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
        modifier = Modifier.size(56.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
