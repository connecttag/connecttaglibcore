package org.connecttag.lib.kotlin.core.text

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/** Represents text that can be either a hardcoded string or a string resource. */
sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    data object Empty : UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
            Empty -> ""
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
            Empty -> ""
        }
    }

    companion object {
        fun from(value: String?, fallback: UiText = Empty): UiText {
            val cleaned = value?.trim()?.takeIf { it.isNotEmpty() } ?: return fallback
            return DynamicString(cleaned)
        }
    }
}
