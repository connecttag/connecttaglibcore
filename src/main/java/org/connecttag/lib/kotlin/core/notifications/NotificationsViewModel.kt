package org.connecttag.lib.kotlin.core.notifications

import androidx.lifecycle.viewModelScope
import org.connecttag.lib.kotlin.core.mvi.BaseMviViewModel
import org.connecttag.lib.kotlin.core.mvi.MviAction
import org.connecttag.lib.kotlin.core.mvi.MviState
import org.connecttag.lib.kotlin.core.mvi.NoMviEffect
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.connecttag.lib.kotlin.core.coroutine.DefaultDispatcherProvider

class NotificationsViewModel(
    private val notificationsRepository: ServerNotificationsRepository,
    private val inboxRepository: NotificationInboxRepository,
    private val uiMapper: ServerNotificationUiMapper,
    private val dispatcher: CoroutineDispatcher = DefaultDispatcherProvider.io,
) : BaseMviViewModel<NotificationsUiState, NotificationsAction, NoMviEffect>(
    initialState = NotificationsUiState(),
    reducer = { state, action ->
        when (action) {
            NotificationsAction.LoadingStarted -> state.copy(isLoading = true, loadFailed = false)
            is NotificationsAction.FeedLoaded -> state.copy(
                title = action.title,
                description = action.description,
                notifications = action.notifications,
                loadFailed = false,
                isLoading = false,
            )
            NotificationsAction.FeedFailed -> state.copy(
                loadFailed = true,
                isLoading = false,
            )
            is NotificationsAction.ReadIdsChanged -> state.copy(readIds = action.readIds)
            is NotificationsAction.ArchivePending -> state.copy(
                hiddenIds = state.hiddenIds + action.notificationId,
                recentlyArchivedId = action.notificationId,
                archiveFailed = false,
            )
            is NotificationsAction.ArchiveUndone -> state.copy(
                hiddenIds = state.hiddenIds - action.notificationId,
                recentlyArchivedId = null,
            )
            is NotificationsAction.ArchiveCommitted -> state.copy(recentlyArchivedId = null)
            is NotificationsAction.ArchiveFailed -> state.copy(
                hiddenIds = state.hiddenIds - action.notificationId,
                recentlyArchivedId = null,
                archiveFailed = true,
            )
        }
    },
) {
    private var hasLoadedNotifications = false
    private var isFetchRunning = false
    private val archiveJobs = mutableMapOf<String, Job>()

    fun loadNotifications(forceRefresh: Boolean = false) {
        if (isFetchRunning) return
        if (!forceRefresh && hasLoadedNotifications) return

        isFetchRunning = true
        viewModelScope.launch(dispatcher) {
            try {
                dispatch(NotificationsAction.LoadingStarted)

                val notificationsResult = notificationsRepository.fetchNotifications(forceRefresh)

                notificationsResult.fold(
                    onSuccess = { response ->
                        val uiItems = response.items?.map { uiMapper.map(it) }.orEmpty()
                        dispatch(NotificationsAction.FeedLoaded(
                            title = response.titleEn, // Or based on locale
                            description = response.descriptionEn,
                            notifications = uiItems
                        ))
                        hasLoadedNotifications = true
                    },
                    onFailure = {
                        dispatch(NotificationsAction.FeedFailed)
                    },
                )
            } finally {
                isFetchRunning = false
            }
        }
    }

    fun markAllRead() {
        // Implementation depends on repository capabilities
    }

    fun archiveNotification(notificationId: String) {
        val notification = currentState.notifications.firstOrNull { it.id == notificationId } ?: return
        if (!notification.isRuntimeInbox || notificationId in currentState.hiddenIds) return
        archiveJobs.remove(notificationId)?.cancel()
        dispatch(NotificationsAction.ArchivePending(notificationId))
        archiveJobs[notificationId] = viewModelScope.launch(dispatcher) {
            delay(5000L)
            inboxRepository.dismissInboxNotification(notificationId).fold(
                onSuccess = { dispatch(NotificationsAction.ArchiveCommitted(notificationId)) },
                onFailure = { dispatch(NotificationsAction.ArchiveFailed(notificationId)) },
            )
            archiveJobs.remove(notificationId)
        }
    }

    fun undoArchive() {
        val notificationId = currentState.recentlyArchivedId ?: return
        archiveJobs.remove(notificationId)?.cancel()
        dispatch(NotificationsAction.ArchiveUndone(notificationId))
    }
}

data class NotificationsUiState(
    val title: String? = null,
    val description: String? = null,
    val notifications: List<ServerNotificationUiItem> = emptyList(),
    val readIds: Set<String> = emptySet(),
    val loadFailed: Boolean = false,
    val isLoading: Boolean = false,
    val hiddenIds: Set<String> = emptySet(),
    val recentlyArchivedId: String? = null,
    val archiveFailed: Boolean = false,
) : MviState {
    val visibleNotifications: List<ServerNotificationUiItem>
        get() = notifications.filterNot { it.id in hiddenIds }
    val unreadCount: Int
        get() = notifications.count { item -> item.id !in readIds }
}

sealed interface NotificationsAction : MviAction {
    data object LoadingStarted : NotificationsAction
    data class FeedLoaded(
        val title: String?,
        val description: String?,
        val notifications: List<ServerNotificationUiItem>
    ) : NotificationsAction
    data object FeedFailed : NotificationsAction
    data class ReadIdsChanged(val readIds: Set<String>) : NotificationsAction
    data class ArchivePending(val notificationId: String) : NotificationsAction
    data class ArchiveUndone(val notificationId: String) : NotificationsAction
    data class ArchiveCommitted(val notificationId: String) : NotificationsAction
    data class ArchiveFailed(val notificationId: String) : NotificationsAction
}
