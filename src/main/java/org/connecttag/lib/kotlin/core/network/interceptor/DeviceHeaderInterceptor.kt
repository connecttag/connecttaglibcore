package org.connecttag.lib.kotlin.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import org.connecttag.lib.kotlin.core.network.DeviceIdentifierProvider

/**
 * An interceptor that adds standard device and application headers to every request.
 */
class DeviceHeaderInterceptor(
    private val identifierProvider: DeviceIdentifierProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        requestBuilder.apply {
            addHeader("X-Device-ID", identifierProvider.getDeviceId())
            addHeader("X-App-Version", identifierProvider.getAppVersion())
            addHeader("X-App-Version-Code", identifierProvider.getAppVersionCode())
            addHeader("X-Platform", "Android")
            addHeader("X-OS-Version", android.os.Build.VERSION.RELEASE)
            addHeader("X-Device-Model", android.os.Build.MODEL)
        }

        return chain.proceed(requestBuilder.build())
    }
}
