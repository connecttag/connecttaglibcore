package org.connecttag.lib.kotlin.core.text

/** Plain text whose value comes from a server, user input, or another runtime source. */
sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    data object Empty : UiText()

    companion object {
        fun from(value: String?, fallback: UiText = Empty): UiText {
            val cleaned = value?.trim()?.takeIf { it.isNotEmpty() } ?: return fallback
            return DynamicString(cleaned)
        }
    }
}
