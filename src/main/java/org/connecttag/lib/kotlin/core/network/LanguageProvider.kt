package org.connecttag.lib.kotlin.core.network

/**
 * Interface to provide the current application language for network requests.
 */
interface LanguageProvider {
    /**
     * Returns the current language code (e.g., "ar", "en").
     */
    fun getLanguage(): String
}
