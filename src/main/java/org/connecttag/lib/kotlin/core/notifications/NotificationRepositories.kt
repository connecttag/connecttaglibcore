package org.connecttag.lib.kotlin.core.notifications

import kotlinx.coroutines.flow.Flow

interface NotificationInboxRepository {
    suspend fun fetchInboxNotifications(
        status: String? = null,
        cursor: String? = null,
        limit: Int = 20,
    ): Result<NotificationInboxUiPage>

    suspend fun fetchInboxSummary(): Result<NotificationInboxSummary>
    suspend fun fetchInboxNotification(id: String): Result<ServerNotificationItemResponse>
    suspend fun markInboxNotificationRead(id: String): Result<Unit>
    suspend fun dismissInboxNotification(id: String): Result<Unit>
}

data class NotificationInboxUiPage(
    val items: List<ServerNotificationItemResponse>,
    val unreadCount: Int,
    val totalCount: Int,
    val nextCursor: String?,
)

data class NotificationInboxSummary(
    val unreadCount: Int,
    val totalCount: Int,
)

interface ServerNotificationsRepository {
    suspend fun fetchNotifications(forceRefresh: Boolean = false): Result<ServerNotificationsResponse>
    suspend fun getCachedNotifications(): Result<ServerNotificationsResponse>
}
