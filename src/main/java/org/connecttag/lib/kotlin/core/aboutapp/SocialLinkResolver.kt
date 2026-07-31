package org.connecttag.lib.kotlin.core.aboutapp

import android.content.Context
import android.content.Intent
import android.net.Uri

class SocialLinkResolver(private val context: Context) {

    fun openSocialLink(link: AboutAppSocialLink) {
        val network = link.network
        val value = link.value

        when (network.kind) {
            SocialLinkKind.Email -> sendEmail(value)
            SocialLinkKind.Store -> openStore(value)
            SocialLinkKind.Url -> openUrl(network.appUrlPrefix + value, network.webUrlPrefix + value)
        }
    }

    private fun openUrl(appUrl: String?, webUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        if (appUrl != null) {
            intent.data = Uri.parse(appUrl)
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                return
            }
        }
        intent.data = Uri.parse(webUrl)
        context.startActivity(intent)
    }

    private fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
        }
        context.startActivity(Intent.createChooser(intent, "Send Email"))
    }

    private fun openStore(packageName: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("market://details?id=$packageName")
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            val webIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            }
            context.startActivity(webIntent)
        }
    }
}
