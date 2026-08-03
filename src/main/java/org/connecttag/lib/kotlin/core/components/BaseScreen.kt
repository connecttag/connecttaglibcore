package org.connecttag.lib.kotlin.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.connecttag.lib.kotlin.core.R
import org.connecttag.lib.kotlin.core.network.connectivity.ConnectivityMonitor
import org.connecttag.lib.kotlin.core.update.AppStatusManager
import org.connecttag.lib.kotlin.core.uimodel.PageState
import org.connecttag.lib.kotlin.core.utils.logDebug

/**
 * A standard screen layout that integrates Scaffold, TopBar, State handling,
 * and automatic App Status / Connectivity monitoring.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> BaseScreen(
    title: String,
    state: PageState<T>,
    appStatusManager: AppStatusManager? = null,
    connectivityMonitor: ConnectivityMonitor? = null,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
    topBar: @Composable () -> Unit = {
        BaseTopAppBar(
            title = title,
            subtitle = subtitle,
            navigationIcon = navigationIcon,
            actions = { actions?.invoke() }
        )
    },
    onRetry: (() -> Unit)? = null,
    onRefresh: (() -> Unit)? = null,
    isRefreshing: Boolean = false,
    loadingMessage: String? = null,
    emptyMessage: String? = null,
    shimmerContent: @Composable (() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    bottomBar: @Composable (() -> Unit)? = null,
    content: @Composable (T) -> Unit
) {
    if (appStatusManager != null && connectivityMonitor != null) {
        AppConnectTagScreen(
            appStatusManager = appStatusManager,
            connectivityMonitor = connectivityMonitor
        ) { isConnected, appStatus ->
            BaseScreenContent(
                title = title,
                state = state,
                modifier = modifier,
                subtitle = subtitle,
                navigationIcon = navigationIcon,
                actions = actions,
                topBar = topBar,
                onRetry = onRetry,
                onRefresh = onRefresh,
                isRefreshing = isRefreshing,
                loadingMessage = loadingMessage,
                emptyMessage = emptyMessage,
                shimmerContent = shimmerContent,
                floatingActionButton = floatingActionButton,
                bottomBar = bottomBar,
                content = content
            )
        }
    } else {
        BaseScreenContent(
            title = title,
            state = state,
            modifier = modifier,
            subtitle = subtitle,
            navigationIcon = navigationIcon,
            actions = actions,
            topBar = topBar,
            onRetry = onRetry,
            onRefresh = onRefresh,
            isRefreshing = isRefreshing,
            loadingMessage = loadingMessage,
            emptyMessage = emptyMessage,
            shimmerContent = shimmerContent,
            floatingActionButton = floatingActionButton,
            bottomBar = bottomBar,
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> BaseScreenContent(
    title: String,
    state: PageState<T>,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
    topBar: @Composable () -> Unit,
    onRetry: (() -> Unit)? = null,
    onRefresh: (() -> Unit)? = null,
    isRefreshing: Boolean = false,
    loadingMessage: String? = null,
    emptyMessage: String? = null,
    shimmerContent: @Composable (() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    bottomBar: @Composable (() -> Unit)? = null,
    content: @Composable (T) -> Unit
) {
    BaseScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = topBar,
        floatingActionButton = floatingActionButton,
        bottomBar = bottomBar,
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (onRefresh != null) {
                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
                        onRefresh = onRefresh,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        BaseStateWrapper(
                            state = state,
                            onRetry = onRetry,
                            loadingMessage = loadingMessage,
                            emptyMessage = emptyMessage,
                            shimmerContent = shimmerContent,
                            content = content
                        )
                    }
                } else {
                    BaseStateWrapper(
                        state = state,
                        onRetry = onRetry,
                        loadingMessage = loadingMessage,
                        emptyMessage = emptyMessage,
                        shimmerContent = shimmerContent,
                        content = content
                    )
                }
            }
        }
    )
}

@Composable
internal fun ConnectivityWarningBanner() {
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
