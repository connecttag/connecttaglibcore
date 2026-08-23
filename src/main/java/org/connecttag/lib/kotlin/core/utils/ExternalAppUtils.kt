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
        val formattedUrl = if (!url.contains("://")) "https://$url" else url
        try {
            val intent = Intent(Intent.ACTION_VIEW, formattedUrl.toUri())
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Intelligent social link opener.
     */
    fun openSocialLink(context: Context, type: String, value: String) {
        if (value.isBlank()) return

        when (type.uppercase()) {
            "FACEBOOK" -> {
                val webUrl = if (value.contains("facebook.com")) value else "https://facebook.com/$value"
                val id = value.substringAfterLast("/").trim('/').substringBefore("?")
                openFacebook(context, id, webUrl)
            }
            "TELEGRAM" -> {
                val username = value.substringAfterLast("/").trim('/').removePrefix("@")
                openTelegram(context, username)
            }
            "WHATSAPP" -> {
                val number = value.replace(Regex("[^0-9]"), "")
                openWhatsApp(context, number)
            }
            "INSTAGRAM" -> {
                val username = value.substringAfterLast("/").trim('/')
                val url = "https://instagram.com/_u/$username"
                try {
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    intent.setPackage("com.instagram.android")
                    if (context !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    openBrowser(context, "https://instagram.com/$username")
                }
            }
            "X", "TWITTER" -> {
                val username = value.substringAfterLast("/").trim('/').removePrefix("@")
                val url = "twitter://user?screen_name=$username"
                try {
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    if (context !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    openBrowser(context, "https://x.com/$username")
                }
            }
            else -> openBrowser(context, value)
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
 * Extension to open a social link.
 */
fun Context.openSocialLink(type: String, value: String) {
    ExternalAppUtils.openSocialLink(this, type, value)
}

/**
 * Extension to open Play Store.
 */
fun Context.openPlayStore(target: String) {
    ExternalAppUtils.openPlayStore(this, target)
}
