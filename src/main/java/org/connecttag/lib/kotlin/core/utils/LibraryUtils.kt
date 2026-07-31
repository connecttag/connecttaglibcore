package org.connecttag.lib.kotlin.core.utils

import java.util.Locale

/**
 * Interface for enums that have a specific value used in serialized forms.
 */
interface WireValueEnum {
    val wireValue: String
}

/**
 * A stable lowercase implementation that doesn't depend on the system locale.
 */
fun String.lowercaseStable(): String {
    return this.lowercase(Locale.ROOT)
}

/**
 * Utility for handling external URI policies.
 */
object ExternalUriPolicy {
    /**
     * Checks if the given URI is a supported absolute URI (e.g., starts with http:// or https://).
     */
    fun isSupportedAbsoluteUri(uri: String): Boolean {
        return uri.startsWith("http://", ignoreCase = true) ||
               uri.startsWith("https://", ignoreCase = true) ||
               uri.startsWith("fb://", ignoreCase = true) ||
               uri.startsWith("twitter://", ignoreCase = true) ||
               uri.startsWith("instagram://", ignoreCase = true) ||
               uri.startsWith("market://", ignoreCase = true) ||
               uri.startsWith("mailto:", ignoreCase = true)
    }
}
