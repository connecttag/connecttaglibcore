package org.connecttag.lib.kotlin.core.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.*

/**
 * Utility for managing application locale and language settings.
 */
object LocaleUtils {

    /**
     * Updates the context with a new locale.
     * This is typically used in [ContextWrapper] and [attachBaseContext].
     * 
     * @param context The base context.
     * @param locale The locale to apply.
     * @return A new context wrapper with the applied locale.
     */
    fun updateLocale(context: Context, locale: Locale): ContextWrapper {
        var currentContext = context
        val resources = currentContext.resources
        val configuration = resources.configuration

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
        } else {
            @Suppress("DEPRECATION")
            configuration.locale = locale
        }

        currentContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            currentContext.createConfigurationContext(configuration)
        } else {
            @Suppress("DEPRECATION")
            resources.updateConfiguration(configuration, resources.displayMetrics)
            currentContext
        }

        return ContextWrapper(currentContext)
    }

    /**
     * Returns the current system locale.
     */
    fun getSystemLocale(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault().get(0)
        } else {
            @Suppress("DEPRECATION")
            Locale.getDefault()
        }
    }
}
