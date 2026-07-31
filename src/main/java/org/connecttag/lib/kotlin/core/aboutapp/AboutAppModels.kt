package org.connecttag.lib.kotlin.core.aboutapp

import kotlinx.serialization.Serializable

@Serializable
data class AboutAppProfile(
    val id: String,
    val name: String? = null,
    val nameKey: String? = null,
    val description: String? = null,
    val descriptionKey: String? = null,
    val logo: String? = null,
    val cover: String? = null,
    val address: String? = null,
    val phoneNumbers: List<String> = emptyList(),
    val imageKey: String? = null,
    val titleKey: String? = null,
    val socialLinks: List<AboutAppSocialLink> = emptyList(),
    val stores: List<AppStoreSpec> = emptyList(),
    val privacyPolicyUrl: String? = null,
    val termsOfUseUrl: String? = null,
    val metadata: Map<String, String> = emptyMap(),
)

@Serializable
data class AppStoreSpec(
    val type: String,
    val url: String,
    val label: String? = null
)

@Serializable
data class AboutAppSocialLink(
    val network: SocialNetworkSpec,
    val value: String,
    val active: Boolean = true,
    val label: String? = null,
)

@Serializable
data class AboutData(
    val type: SocialNetworkPlatform,
    val url: String,
    val active: Boolean = true,
)

fun AboutData.toAboutAppSocialLink(): AboutAppSocialLink {
    return AboutAppSocialLink(
        network = type.toSpec(),
        value = url,
        active = active,
    )
}
