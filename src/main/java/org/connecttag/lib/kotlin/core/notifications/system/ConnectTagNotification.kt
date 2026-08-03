package org.connecttag.lib.kotlin.core.notifications.system

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.connecttag.lib.kotlin.core.text.UiText

class ConnectTagNotification(
    override val context: Context,
    private val receiverClass: Class<*>,
    @DrawableRes private val defaultSmallIcon: Int
) : NotificationManager {

    private val notificationManager = NotificationManagerCompat.from(context)

    fun createChannel(id: String, name: String, importance: Int = android.app.NotificationManager.IMPORTANCE_DEFAULT) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(id, name, importance)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun createNotification(
        channelId: String,
        title: UiText,
        description: UiText,
        actions: Collection<NotificationCompat.Action>,
        showTimestamp: Boolean,
        importance: Int,
        onGoing: Boolean,
        onlyAlertOnce: Boolean,
        groupKey: String?,
        isGroupSummary: Boolean,
        largeIcon: Bitmap?,
        smallIcon: Int?
    ): Notification {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIcon ?: defaultSmallIcon)
            .setContentTitle(title.asString(context))
            .setContentText(description.asString(context))
            .setPriority(importanceToPriority(importance))
            .setAutoCancel(!onGoing)
            .setOngoing(onGoing)
            .setShowWhen(showTimestamp)
            .setOnlyAlertOnce(onlyAlertOnce)
            .setGroup(groupKey)
            .setGroupSummary(isGroupSummary)

        largeIcon?.let { builder.setLargeIcon(it) }
        actions.forEach { builder.addAction(it) }

        return builder.build()
    }

    override fun createNotificationAction(
        actionType: String,
        title: String,
        icon: Int?,
        extraId: String?,
        extraData: String?,
        bundle: Bundle?
    ): NotificationCompat.Action {
        val intent = Intent(context, receiverClass).apply {
            action = actionType
            putExtra(NotificationManager.EXTRA_ACTION_TYPE, actionType)
            putExtra(NotificationManager.EXTRA_ID, extraId)
            putExtra(NotificationManager.EXTRA_DATA, extraData)
            bundle?.let { putExtras(it) }
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            (extraId?.hashCode() ?: 0) + actionType.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Action.Builder(
            icon ?: 0,
            title,
            pendingIntent
        ).build()
    }

    override fun show(notificationId: Int, notification: Notification) {
        try {
            notificationManager.notify(notificationId, notification)
        } catch (e: SecurityException) {
            // Log or handle missing POST_NOTIFICATIONS permission on Android 13+
        }
    }

    override fun remove(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    private fun importanceToPriority(importance: Int): Int {
        return when (importance) {
            android.app.NotificationManager.IMPORTANCE_HIGH -> NotificationCompat.PRIORITY_HIGH
            android.app.NotificationManager.IMPORTANCE_LOW -> NotificationCompat.PRIORITY_LOW
            android.app.NotificationManager.IMPORTANCE_MIN -> NotificationCompat.PRIORITY_MIN
            else -> NotificationCompat.PRIORITY_DEFAULT
        }
    }
}
