package org.connecttag.lib.kotlin.core.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.connecttag.lib.kotlin.core.R
import org.connecttag.lib.kotlin.core.actions.ActionRequestHandler
import org.connecttag.lib.kotlin.core.ui.components.scaffold.AkScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    uiState: NotificationsUiState,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
    onOpenNotification: (String) -> Unit,
    onMarkAllRead: () -> Unit,
    onArchive: (String) -> Unit,
    onUndoArchive: () -> Unit,
    onAction: ActionRequestHandler,
) {
    var selectedFilter by remember { mutableStateOf(NotificationFilter.ALL) }

    AkScaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.title ?: stringResource(R.string.notifications_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.notifications_navigation_back_content_description)
                        )
                    }
                },
                actions = {
                    if (uiState.unreadCount > 0) {
                        TextButton(onClick = onMarkAllRead) {
                            Text(stringResource(R.string.notifications_mark_all_read))
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                uiState.isLoading && uiState.notifications.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.loadFailed && uiState.notifications.isEmpty() -> {
                    NotificationEmptyState(
                        title = stringResource(R.string.notifications_empty_title),
                        message = stringResource(R.string.notifications_update_failed),
                        onAction = onRefresh,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                uiState.notifications.isEmpty() -> {
                    NotificationEmptyState(
                        title = stringResource(R.string.notifications_empty_title),
                        message = uiState.description ?: stringResource(R.string.notifications_empty_message),
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    val visibleNotifications = uiState.visibleNotifications.filter { notification ->
                        selectedFilter.matches(notification, uiState.readIds)
                    }
                    val refreshState = rememberPullToRefreshState()
                    PullToRefreshBox(
                        isRefreshing = uiState.isLoading,
                        onRefresh = onRefresh,
                        state = refreshState,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            item {
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(NotificationFilter.entries.size) { index ->
                                        val filter = NotificationFilter.entries[index]
                                        FilterChip(
                                            selected = selectedFilter == filter,
                                            onClick = { selectedFilter = filter },
                                            label = { Text(filter.label()) },
                                        )
                                    }
                                }
                            }

                            items(visibleNotifications.size, key = { visibleNotifications[it].id }) { index ->
                                val notification = visibleNotifications[index]
                                val dismissState = rememberSwipeToDismissBoxState(
                                    confirmValueChange = { value ->
                                        if (value == SwipeToDismissBoxValue.EndToStart && notification.isRuntimeInbox) {
                                            onArchive(notification.id)
                                        }
                                        false
                                    },
                                )
                                SwipeToDismissBox(
                                    state = dismissState,
                                    enableDismissFromStartToEnd = false,
                                    enableDismissFromEndToStart = notification.isRuntimeInbox,
                                    backgroundContent = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(MaterialTheme.colorScheme.errorContainer)
                                                .padding(horizontal = 20.dp),
                                            contentAlignment = Alignment.CenterEnd,
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.Archive,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                                modifier = Modifier.size(24.dp),
                                            )
                                        }
                                    },
                                ) {
                                    NotificationCard(
                                        notification = notification,
                                        isUnread = notification.id !in uiState.readIds,
                                        onOpen = { onOpenNotification(notification.id) },
                                        onAction = onAction,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (uiState.recentlyArchivedId != null || uiState.archiveFailed) {
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                    action = if (uiState.recentlyArchivedId != null) {
                        { TextButton(onClick = onUndoArchive) { Text(stringResource(R.string.notifications_undo)) } }
                    } else null,
                ) {
                    Text(stringResource(if (uiState.archiveFailed) R.string.notifications_archive_failed else R.string.notifications_archived))
                }
            }
        }
    }
}

@Composable
private fun NotificationEmptyState(
    title: String,
    message: String?,
    modifier: Modifier = Modifier,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        if (message != null) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        if (onAction != null) {
            Button(onClick = onAction, modifier = Modifier.padding(top = 16.dp)) {
                Text(text = stringResource(R.string.notifications_refresh))
            }
        }
    }
}

private enum class NotificationFilter {
    ALL, UNREAD, ORDERS, OFFERS, SECURITY;

    fun matches(item: ServerNotificationUiItem, readIds: Set<String>): Boolean {
        return when (this) {
            ALL -> true
            UNREAD -> item.id !in readIds
            ORDERS -> item.category == ServerNotificationCategory.ORDER
            OFFERS -> item.category in setOf(
                ServerNotificationCategory.OFFER,
                ServerNotificationCategory.PROMOTION,
            )
            SECURITY -> item.category in setOf(
                ServerNotificationCategory.SECURITY,
                ServerNotificationCategory.ALERT,
            )
        }
    }
}

@Composable
private fun NotificationFilter.label(): String = stringResource(when (this) {
    NotificationFilter.ALL -> R.string.notifications_filter_all
    NotificationFilter.UNREAD -> R.string.notifications_filter_unread
    NotificationFilter.ORDERS -> R.string.notifications_filter_orders
    NotificationFilter.OFFERS -> R.string.notifications_filter_offers
    NotificationFilter.SECURITY -> R.string.notifications_filter_security
})
