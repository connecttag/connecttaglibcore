package org.connecttag.lib.kotlin.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.connecttag.lib.kotlin.core.R
import org.connecttag.lib.kotlin.core.utils.ConnectivityObserver
import org.connecttag.lib.kotlin.core.utils.PageState

/**
 * A standard screen layout that integrates Scaffold, TopBar, and State handling.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> BaseScreen(
    title: String,
    state: PageState<T>,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
    onRetry: (() -> Unit)? = null,
    loadingMessage: String? = null,
    emptyMessage: String? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    bottomBar: @Composable (() -> Unit)? = null,
    networkStatus: ConnectivityObserver.Status? = null,
    content: @Composable (T) -> Unit
) {
    BaseScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            BaseTopAppBar(
                title = title,
                subtitle = subtitle,
                navigationIcon = navigationIcon,
                actions = { actions?.invoke() }
            )
        },
        floatingActionButton = floatingActionButton,
        bottomBar = bottomBar,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Connectivity Warning Banner
                val isOffline = networkStatus != null && 
                    (networkStatus == ConnectivityObserver.Status.Lost || networkStatus == ConnectivityObserver.Status.Unavailable)
                
                AnimatedVisibility(
                    visible = isOffline,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    ConnectivityWarningBanner()
                }

                Box(modifier = Modifier.weight(1f)) {
                    BaseStateWrapper(
                        state = state,
                        onRetry = onRetry,
                        loadingMessage = loadingMessage,
                        emptyMessage = emptyMessage,
                        content = content
                    )
                }
            }
        }
    )
}

@Composable
private fun ConnectivityWarningBanner() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.errorContainer,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.WifiOff,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = stringResource(R.string.no_internet_connection),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
