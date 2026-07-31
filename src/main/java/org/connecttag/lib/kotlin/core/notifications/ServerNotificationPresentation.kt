package org.connecttag.lib.kotlin.core.notifications

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.SettingsSuggest
import androidx.compose.material.icons.outlined.SystemUpdate
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import org.connecttag.lib.kotlin.core.R

@Composable
internal fun notificationCategoryLabel(
    category: ServerNotificationCategory,
    rawCategory: String?,
): String = when (category) {
    ServerNotificationCategory.ORDER -> stringResource(R.string.notifications_type_order)
    ServerNotificationCategory.PROMOTION,
    ServerNotificationCategory.OFFER -> stringResource(R.string.notifications_type_offer)
    ServerNotificationCategory.SECURITY -> stringResource(R.string.notifications_type_security)
    ServerNotificationCategory.UPDATE -> stringResource(R.string.notifications_type_update)
    ServerNotificationCategory.SERVICE -> stringResource(R.string.notifications_type_service)
    ServerNotificationCategory.TECHNICAL,
    ServerNotificationCategory.ALERT,
    ServerNotificationCategory.GIFT,
    ServerNotificationCategory.INFO,
    ServerNotificationCategory.UNKNOWN -> rawCategory?.takeIf(String::isNotBlank)
        ?: stringResource(R.string.notifications_screen_title)
}

@Composable
internal fun notificationPriorityLabel(priority: ServerNotificationPriority): String? = when {
    priority.isUrgent -> stringResource(R.string.notifications_priority_urgent)
    priority == ServerNotificationPriority.HIGH -> stringResource(R.string.notifications_priority_high)
    else -> null
}

internal fun ServerNotificationCategory.notificationIcon(): ImageVector = when (this) {
    ServerNotificationCategory.PROMOTION,
    ServerNotificationCategory.OFFER -> Icons.Outlined.LocalOffer
    ServerNotificationCategory.TECHNICAL -> Icons.Outlined.SettingsSuggest
    ServerNotificationCategory.UPDATE -> Icons.Outlined.SystemUpdate
    ServerNotificationCategory.ALERT -> Icons.Outlined.WarningAmber
    ServerNotificationCategory.GIFT -> Icons.Outlined.AutoAwesome
    ServerNotificationCategory.INFO -> Icons.Outlined.Info
    ServerNotificationCategory.ORDER,
    ServerNotificationCategory.SECURITY,
    ServerNotificationCategory.SERVICE,
    ServerNotificationCategory.UNKNOWN -> Icons.Outlined.Notifications
}
