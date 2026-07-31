package org.connecttag.lib.kotlin.core.notifications

import java.util.Locale

class ServerNotificationUiMapper(
    private val locale: Locale = Locale.getDefault()
) {
    fun map(
        response: ServerNotificationItemResponse,
        isRuntimeInbox: Boolean = false
    ): ServerNotificationUiItem {
        val isArabic = locale.language == "ar"
        return ServerNotificationUiItem(
            id = response.id.orEmpty(),
            title = if (isArabic) response.titleAr.orEmpty() else response.titleEn.orEmpty(),
            message = if (isArabic) response.bodyAr.orEmpty() else response.bodyEn.orEmpty(),
            category = mapCategory(response.category),
            rawCategory = response.category,
            priority = mapPriority(response.priority),
            timestamp = response.createdAt,
            imageUrl = response.metadata?.get("imageUrl"),
            actions = response.actions?.map { action ->
                ServerNotificationUiAction(
                    id = action.id.orEmpty(),
                    label = if (isArabic) action.labelAr else action.labelEn,
                    type = action.actionType.orEmpty(),
                    content = action.actionContent
                )
            }.orEmpty(),
            isRuntimeInbox = isRuntimeInbox
        )
    }

    private fun mapCategory(raw: String?): ServerNotificationCategory {
        return when (raw?.lowercase()) {
            "order" -> ServerNotificationCategory.ORDER
            "offer" -> ServerNotificationCategory.OFFER
            "promotion" -> ServerNotificationCategory.PROMOTION
            "security" -> ServerNotificationCategory.SECURITY
            "update" -> ServerNotificationCategory.UPDATE
            "service" -> ServerNotificationCategory.SERVICE
            "technical" -> ServerNotificationCategory.TECHNICAL
            "alert" -> ServerNotificationCategory.ALERT
            "gift" -> ServerNotificationCategory.GIFT
            "info" -> ServerNotificationCategory.INFO
            else -> ServerNotificationCategory.UNKNOWN
        }
    }

    private fun mapPriority(raw: String?): ServerNotificationPriority {
        return when (raw?.lowercase()) {
            "low" -> ServerNotificationPriority.LOW
            "high" -> ServerNotificationPriority.HIGH
            "urgent" -> ServerNotificationPriority.URGENT
            else -> ServerNotificationPriority.NORMAL
        }
    }
}
