package org.connecttag.lib.kotlin.core.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build

/**
 * Utility for interacting with the Android Clipboard.
 */
object ClipboardUtils {

    /**
     * Copies the given [text] to the clipboard.
     * 
     * @param context Android context to access clipboard service.
     * @param text The text to copy.
     * @param label A user-visible label for the clip data.
     */
    fun copyToClipboard(context: Context, text: String, label: String = "Label") {
        if (text.isBlank()) return
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
    }

    /**
     * Retrieves the primary clip text from the clipboard.
     * 
     * @param context Android context.
     * @return The text content of the clipboard, or null if empty.
     */
    fun pasteFromClipboard(context: Context): String? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip ?: return null
        if (clip.itemCount > 0) {
            return clip.getItemAt(0).coerceToText(context).toString()
        }
        return null
    }

    /**
     * Clears the clipboard content.
     */
    fun clearClipboard(context: Context) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            clipboard.clearPrimaryClip()
        } else {
            @Suppress("DEPRECATION")
            clipboard.text = ""
        }
    }
}

/**
 * Extension function to copy text to clipboard.
 */
fun Context.copyToClipboard(text: String, label: String = "Label") {
    ClipboardUtils.copyToClipboard(this, text, label)
}

/**
 * Extension function to paste text from clipboard.
 */
fun Context.pasteFromClipboard(): String? {
    return ClipboardUtils.pasteFromClipboard(this)
}
