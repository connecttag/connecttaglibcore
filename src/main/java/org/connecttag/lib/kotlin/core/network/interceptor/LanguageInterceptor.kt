package org.connecttag.lib.kotlin.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import org.connecttag.lib.kotlin.core.network.LanguageProvider

/**
 * An interceptor that adds language headers to every request.
 */
class LanguageInterceptor(
    private val languageProvider: LanguageProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val language = languageProvider.getLanguage()
        val request = chain.request().newBuilder()
            .addHeader("Accept-Language", language)
            .addHeader("lang", language)
            .build()
        return chain.proceed(request)
    }
}
