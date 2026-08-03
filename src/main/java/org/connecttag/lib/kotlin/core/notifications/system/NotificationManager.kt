package org.connecttag.lib.kotlin.core.notifications.system

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import org.connecttag.lib.kotlin.core.text.UiText

interface NotificationManager {
    val context: Context

    fun createNotification(
        channelId: String,
        title: UiText,
        description: UiText,
        actions: Collection<NotificationCompat.Action> = emptyList(),
        showTimestamp: Boolean = true,
        importance: Int = android.app.NotificationManager.IMPORTANCE_DEFAULT,
        onGoing: Boolean = false,
        onlyAlertOnce: Boolean = true,
        groupKey: String? = null,
        isGroupSummary: Boolean = false,
        largeIcon: Bitmap? = null,
        @DrawableRes smallIcon: Int? = null
    ): Notification

    fun createNotificationAction(
        actionType: String,
        title: String,
        @DrawableRes icon: Int? = null,
        extraId: String? = null,
        extraData: String? = null,
        bundle: Bundle? = null
    ): NotificationCompat.Action

    fun show(notificationId: Int, notification: Notification)

    fun remove(notificationId: Int)

    companion object {
        const val EXTRA_ACTION_TYPE = "notification_action_type"
        const val EXTRA_ID = "notification_id"
        const val EXTRA_DATA = "notification_data"

        // Semantic Colors
        val COLOR_PRIMARY = android.graphics.Color.parseColor("#2196F3")
        val COLOR_SUCCESS = android.graphics.Color.parseColor("#4CAF50")
        val COLOR_ERROR = android.graphics.Color.parseColor("#F44336")
    }
}
