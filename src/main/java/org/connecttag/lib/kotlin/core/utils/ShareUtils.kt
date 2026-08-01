package org.connecttag.lib.kotlin.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Utility for sharing content.
 */
object ShareUtils {

    /**
     * Shares [text] using the Android share sheet.
     * 
     * @param context Android context.
     * @param text The content to share.
     * @param title The title of the share sheet.
     */
    fun shareText(context: Context, text: String, title: String? = null) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooserIntent = Intent.createChooser(shareIntent, title)
        if (context !is Activity) {
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooserIntent)
    }
}

/**
 * Extension function to share text.
 */
fun Context.shareText(text: String, title: String? = null) {
    ShareUtils.shareText(this, text, title)
}
