package org.connecttag.lib.kotlin.core.ui.components.state

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.connecttag.lib.kotlin.core.theme.engine.ConnectTagTheme

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    message: String? = null
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        message?.let {
            Spacer(modifier = Modifier.height(ConnectTagTheme.spacing.medium))
            Text(text = it, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun StateMessageView(
    icon: ImageVector,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(ConnectTagTheme.spacing.large),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(ConnectTagTheme.spacing.medium))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(ConnectTagTheme.spacing.small))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (actionLabel != null && onAction != null) {
            Spacer(modifier = Modifier.height(ConnectTagTheme.spacing.large))
            Button(onClick = onAction) {
                Text(text = actionLabel)
            }
        }
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "حدث خطأ ما"
) {
    StateMessageView(
        icon = Icons.Default.ErrorOutline,
        title = title,
        message = message,
        modifier = modifier,
        actionLabel = "إعادة المحاولة",
        onAction = onRetry
    )
}

@Composable
fun EmptyView(
    message: String,
    modifier: Modifier = Modifier,
    title: String = "لا توجد بيانات"
) {
    StateMessageView(
        icon = Icons.Default.SearchOff,
        title = title,
        message = message,
        modifier = modifier
    )
}

@Composable
fun OfflineView(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "لا يوجد اتصال",
    message: String = "يرجى التحقق من اتصالك بالإنترنت والمحاولة مرة أخرى"
) {
    StateMessageView(
        icon = Icons.Default.CloudOff,
        title = title,
        message = message,
        modifier = modifier,
        actionLabel = "إعادة المحاولة",
        onAction = onRetry
    )
}
