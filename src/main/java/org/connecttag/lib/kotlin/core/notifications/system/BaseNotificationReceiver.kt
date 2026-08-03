package org.connecttag.lib.kotlin.core.notifications.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import org.connecttag.lib.kotlin.core.utils.openBrowser

abstract class BaseNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val actionType = intent.getStringExtra(NotificationManager.EXTRA_ACTION_TYPE)
        val extraData = intent.getStringExtra(NotificationManager.EXTRA_DATA)

        when (actionType) {
            ACTION_OPEN_URL -> {
                extraData?.let { context.openBrowser(it) }
            }
            else -> onHandleCustomAction(context, actionType, intent)
        }
    }

    /**
     * Override to handle app-specific notification actions.
     */
    protected open fun onHandleCustomAction(context: Context, actionType: String?, intent: Intent) {
        // To be implemented by subclasses
    }

    companion object {
        const val ACTION_OPEN_URL = "org.connecttag.action.OPEN_URL"
        const val ACTION_DISMISS = "org.connecttag.action.DISMISS"
    }
}
