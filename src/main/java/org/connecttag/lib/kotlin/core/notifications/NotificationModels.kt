package org.connecttag.lib.kotlin.core.notifications

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
enum class ServerNotificationCategory {
    ORDER,
    OFFER,
    PROMOTION,
    SECURITY,
    UPDATE,
    SERVICE,
    TECHNICAL,
    ALERT,
    GIFT,
    INFO,
    UNKNOWN,
}

@Serializable
enum class ServerNotificationPriority {
    LOW,
    NORMAL,
    HIGH,
    URGENT;

    val isUrgent: Boolean get() = this == URGENT
}

@Serializable
data class ServerNotificationsResponse(
    val schemaVersion: Int? = 1,
    val version: Int? = 1,
    val lastUpdated: String? = null,
    val titleAr: String? = null,
    val titleEn: String? = null,
    val descriptionAr: String? = null,
    val descriptionEn: String? = null,
    val items: List<ServerNotificationItemResponse>? = emptyList(),
)

@Serializable
data class ServerNotificationItemResponse(
    val id: String? = null,
    val type: String? = null,
    val titleAr: String? = null,
    val titleEn: String? = null,
    val bodyAr: String? = null,
    val bodyEn: String? = null,
    val active: Boolean? = true,
    val priority: String? = null,
    val category: String? = null,
    val actions: List<ServerNotificationActionResponse>? = emptyList(),
    val metadata: Map<String, String>? = emptyMap(),
    val createdAt: String? = null,
)

@Serializable
data class ServerNotificationActionResponse(
    val id: String? = null,
    val labelAr: String? = null,
    val labelEn: String? = null,
    val labelKey: String? = null,
    val actionType: String? = null,
    val url: String? = null,
    val actionContent: JsonObject? = null,
)

data class ServerNotificationUiItem(
    val id: String,
    val title: String,
    val message: String,
    val category: ServerNotificationCategory,
    val rawCategory: String?,
    val priority: ServerNotificationPriority,
    val timestamp: String?,
    val imageUrl: String? = null,
    val actions: List<ServerNotificationUiAction> = emptyList(),
    val isRuntimeInbox: Boolean = false,
) {
    val ageGroup: NotificationAgeGroup
        get() = NotificationAgeGroup.TODAY // Placeholder, should be calculated based on timestamp
}

data class ServerNotificationUiAction(
    val id: String,
    val label: String?,
    val type: String,
    val content: JsonObject?,
)

enum class NotificationAgeGroup {
    TODAY,
    THIS_WEEK,
    OLDER,
}
