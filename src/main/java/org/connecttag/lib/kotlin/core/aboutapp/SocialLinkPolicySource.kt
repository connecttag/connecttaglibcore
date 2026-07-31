package org.connecttag.lib.kotlin.core.aboutapp

interface SocialLinkPolicySource {
    fun canOpenGooglePlayLinks(): Boolean
}

object AllowAllSocialLinkPolicySource : SocialLinkPolicySource {
    override fun canOpenGooglePlayLinks(): Boolean = true
}
