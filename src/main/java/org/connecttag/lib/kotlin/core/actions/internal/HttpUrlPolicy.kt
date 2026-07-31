package org.connecttag.lib.kotlin.core.actions.internal

object HttpUrlPolicy {
    fun isHttpUrl(value: String?): Boolean {
        val cleaned = value?.trim()?.takeIf { it.isNotEmpty() } ?: return false
        return cleaned.startsWith("http://", ignoreCase = true) ||
            cleaned.startsWith("https://", ignoreCase = true)
    }
}
