package org.connecttag.lib.kotlin.core.aboutapp

import org.connecttag.lib.kotlin.core.utils.WireValueEnum
import org.connecttag.lib.kotlin.core.utils.lowercaseStable
import kotlinx.serialization.Serializable

@Serializable
enum class SocialLinkKind {
    Url,
    Email,
    Store,
}

@Serializable
data class SocialNetworkSpec(
    val key: String,
    val displayName: String,
    val webUrlPrefix: String,
    val appUrlPrefix: String? = webUrlPrefix,
    val androidPackageName: String? = null,
    val iconKey: String = key,
    val kind: SocialLinkKind = SocialLinkKind.Url,
    val active: Boolean = true,
    val aliases: Set<String> = emptySet(),
)

class SocialNetworkCatalog(
    networks: List<SocialNetworkSpec> = defaultSocialNetworkSpecs(),
) {
    private val networkSpecs: List<SocialNetworkSpec> = networks
    private val byKey: Map<String, SocialNetworkSpec> =
        networks.flatMap { network ->
            network.lookupKeys().map { key -> normalizeNetworkKey(key) to network }
        }.toMap()

    fun all(): List<SocialNetworkSpec> = networkSpecs

    fun getByKey(key: String?): SocialNetworkSpec? {
        return key?.let { byKey[normalizeNetworkKey(it)] }
    }

    fun withNetwork(network: SocialNetworkSpec): SocialNetworkCatalog {
        val normalizedKey = normalizeNetworkKey(network.key)
        return SocialNetworkCatalog(
            networkSpecs
                .filterNot { normalizeNetworkKey(it.key) == normalizedKey }
                .plus(network),
        )
    }

    companion object {
        val Default: SocialNetworkCatalog = SocialNetworkCatalog()
    }
}

@Serializable
enum class SocialNetworkPlatform(
    val id: Int,
    val key: String,
    val displayName: String,
    val webUrlPrefix: String,
    val appUrlPrefix: String? = webUrlPrefix,
    val androidPackageName: String? = null,
    val iconKey: String = key,
    val kind: SocialLinkKind = SocialLinkKind.Url,
    val aliases: Set<String> = emptySet(),
) : WireValueEnum {
    Unknown(
        id = 0,
        key = "unknown",
        displayName = "Unknown",
        webUrlPrefix = "",
        appUrlPrefix = null,
    ),
    Whatsapp(
        id = 1,
        key = "whatsapp",
        displayName = "WhatsApp",
        webUrlPrefix = "https://whatsapp.com/channel/",
        appUrlPrefix = "https://whatsapp.com/channel/",
        androidPackageName = "com.whatsapp",
        aliases = setOf("whatsApp", "whatsappChannel", "whatsappGroup"),
    ),
    Facebook(
        id = 2,
        key = "facebook",
        displayName = "Facebook",
        webUrlPrefix = "https://www.facebook.com/",
        appUrlPrefix = "fb://page/",
        androidPackageName = "com.facebook.katana",
        aliases = setOf("fb"),
    ),
    Instagram(
        id = 3,
        key = "instagram",
        displayName = "Instagram",
        webUrlPrefix = "https://instagram.com/",
        appUrlPrefix = "http://instagram.com/_u/",
        androidPackageName = "com.instagram.android",
    ),
    Telegram(
        id = 4,
        key = "telegram",
        displayName = "Telegram",
        webUrlPrefix = "https://t.me/",
        appUrlPrefix = "https://t.me/",
        androidPackageName = "org.telegram.messenger",
    ),
    X(
        id = 5,
        key = "x",
        displayName = "X",
        webUrlPrefix = "https://x.com/",
        appUrlPrefix = "twitter://user?user_id=",
        androidPackageName = "com.twitter.android",
        aliases = setOf("twitter"),
    ),
    Gmail(
        id = 6,
        key = "gmail",
        displayName = "Gmail",
        webUrlPrefix = "",
        appUrlPrefix = null,
        androidPackageName = "com.google.android.gm",
        iconKey = "mail",
        kind = SocialLinkKind.Email,
        aliases = setOf("email", "mail"),
    ),
    Youtube(
        id = 7,
        key = "youtube",
        displayName = "YouTube",
        webUrlPrefix = "https://youtube.com/@",
        appUrlPrefix = "https://youtube.com/@",
        androidPackageName = "com.google.android.youtube",
        aliases = setOf("youTube"),
    ),
    LinkedIn(
        id = 8,
        key = "linkedin",
        displayName = "LinkedIn",
        webUrlPrefix = "https://www.linkedin.com/in/",
        appUrlPrefix = "https://www.linkedin.com/in/",
        androidPackageName = "com.linkedin.android",
    ),
    Threads(
        id = 9,
        key = "threads",
        displayName = "Threads",
        webUrlPrefix = "https://www.threads.net/@",
        appUrlPrefix = "https://www.threads.net/@",
        androidPackageName = "com.instagram.barcelona",
    ),
    Tiktok(
        id = 10,
        key = "tiktok",
        displayName = "TikTok",
        webUrlPrefix = "https://www.tiktok.com/@",
        appUrlPrefix = "https://www.tiktok.com/@",
        androidPackageName = "com.zhiliaoapp.musically",
        aliases = setOf("tikTok"),
    ),
    GooglePlay(
        id = 11,
        key = "googlePlay",
        displayName = "Google Play",
        webUrlPrefix = "https://play.google.com/store/apps/details?id=",
        appUrlPrefix = "market://details?id=",
        androidPackageName = "com.android.vending",
        iconKey = "googlePlay",
        kind = SocialLinkKind.Store,
        aliases = setOf("google", "google play", "playStore", "androidStore", "play"),
    );

    fun toSpec(): SocialNetworkSpec {
        return SocialNetworkSpec(
            key = key,
            displayName = displayName,
            webUrlPrefix = webUrlPrefix,
            appUrlPrefix = appUrlPrefix,
            androidPackageName = androidPackageName,
            iconKey = iconKey,
            kind = kind,
            aliases = aliases,
        )
    }

    override val wireValue: String
        get() = key

    companion object {
        val selectableOptions: List<SocialNetworkPlatform> = entries - Unknown

        fun fromWireValue(rawValue: String?): SocialNetworkPlatform =
            rawValue?.let(::getByName) ?: Unknown

        fun getByName(name: String): SocialNetworkPlatform? {
            val normalized = normalizeNetworkKey(name)
            return entries.find { platform ->
                normalizeNetworkKey(platform.key) == normalized ||
                    normalizeNetworkKey(platform.name) == normalized ||
                    normalizeNetworkKey(platform.displayName) == normalized ||
                    platform.aliases.any { normalizeNetworkKey(it) == normalized }
            }
        }
    }
}

private fun SocialNetworkSpec.lookupKeys(): Set<String> {
    return buildSet {
        add(key)
        add(displayName)
        addAll(aliases)
    }
}

fun defaultSocialNetworkSpecs(): List<SocialNetworkSpec> {
    return SocialNetworkPlatform.selectableOptions.map { it.toSpec() }
}

fun normalizeNetworkKey(value: String): String {
    return value
        .trim()
        .lowercaseStable()
        .replace("_", "")
        .replace("-", "")
        .replace(" ", "")
}
