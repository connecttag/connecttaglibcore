package org.connecttag.lib.kotlin.core.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature

/**
 * A stylized WebView with support for Dark Mode and progress tracking.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BaseWebView(
    url: String,
    modifier: Modifier = Modifier,
    onProgressChange: (Int) -> Unit = {},
    onPageStarted: (String?) -> Unit = {},
    onPageFinished: (String?) -> Unit = {},
    isDarkMode: Boolean = false
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        onPageStarted(url)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onPageFinished(url)
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return false
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        onProgressChange(newProgress)
                    }
                }
            }
        },
        update = { webView ->
            // Handle Dark Mode
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                val forceDark = if (isDarkMode) {
                    WebSettingsCompat.FORCE_DARK_ON
                } else {
                    WebSettingsCompat.FORCE_DARK_OFF
                }
                WebSettingsCompat.setForceDark(webView.settings, forceDark)
            }

            if (webView.url != url) {
                webView.loadUrl(url)
            }
        },
        modifier = modifier.fillMaxSize()
    )
}
