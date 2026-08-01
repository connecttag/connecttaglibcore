package org.connecttag.lib.kotlin.core.utils

import android.content.Context
import android.view.View
import android.widget.Toast

/**
 * Utility for showing Toasts and Snackbars.
 */
object UiMessageUtils {

    fun showToast(context: Context, message: String, isLong: Boolean = false) {
        val duration = if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast.makeText(context, message, duration).show()
    }


}

/**
 * Extension to show a Toast.
 */
fun Context.showToast(message: String, isLong: Boolean = false) {
    UiMessageUtils.showToast(this, message, isLong)
}
