package org.connecttag.lib.kotlin.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.net.toUri

/**
 * Utility for interacting with other applications on the device.
 */
object ExternalAppUtils {

    /**
     * Opens a URL in the web browser.
     */
    fun openBrowser(context: Context, url: String) {
        if (url.isBlank()) return
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Opens the Google Play Store for the given [packageName] or [url].
     */
    fun openPlayStore(context: Context, target: String) {
        val playUrl = if (target.startsWith("http")) target else "market://details?id=$target"
        openBrowser(context, playUrl)
    }

    /**
     * Checks if an app with the given [packageName] is installed.
     */
    fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * Opens a Telegram chat or channel.
     */
    fun openTelegram(context: Context, username: String) {
        val url = "https://t.me/$username"
        openBrowser(context, url)
    }

    /**
     * Opens a Facebook page.
     */
    fun openFacebook(context: Context, pageId: String, webUrl: String) {
        val appUri = "fb://page/$pageId".toUri()
        try {
            val intent = Intent(Intent.ACTION_VIEW, appUri)
            if (context !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            openBrowser(context, webUrl)
        }
    }

    /**
     * Opens a WhatsApp chat with a specific number.
     */
    fun openWhatsApp(context: Context, phoneNumber: String) {
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
        openBrowser(context, url)
    }
}

/**
 * Extension to open a browser.
 */
fun Context.openBrowser(url: String) {
    ExternalAppUtils.openBrowser(this, url)
}

/**
 * Extension to open Play Store.
 */
fun Context.openPlayStore(target: String) {
    ExternalAppUtils.openPlayStore(this, target)
}
