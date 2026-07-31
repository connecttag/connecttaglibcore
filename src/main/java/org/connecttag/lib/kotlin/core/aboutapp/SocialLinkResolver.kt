package org.connecttag.lib.kotlin.core.aboutapp

import org.connecttag.lib.kotlin.core.utils.ExternalUriPolicy

fun interface InstalledAppChecker {
    fun isAppInstalled(packageName: String): Boolean
}

object NoInstalledAppChecker : InstalledAppChecker {
    override fun isAppInstalled(packageName: String): Boolean = false
}

fun resolveSocialLinkAction(
    link: AboutData,
    catalog: SocialNetworkCatalog = SocialNetworkCatalog.Default,
    installedAppChecker: InstalledAppChecker = NoInstalledAppChecker,
): AboutAppAction? {
    if (!link.active) return null
    return resolveSocialLinkAction(
        platform = link.type,
        value = link.url,
        catalog = catalog,
        installedAppChecker = installedAppChecker,
    )
}

fun resolveSocialLinkAction(
    platform: SocialNetworkPlatform,
    value: String,
    catalog: SocialNetworkCatalog = SocialNetworkCatalog.Default,
    installedAppChecker: InstalledAppChecker = NoInstalledAppChecker,
): AboutAppAction {
    val network = catalog.getByKey(platform.key) ?: platform.toSpec()
    return resolveSocialLinkAction(
        network = network,
        value = value,
        installedAppChecker = installedAppChecker,
    )
}

fun resolveSocialLinkAction(
    networkName: String,
    value: String,
    catalog: SocialNetworkCatalog = SocialNetworkCatalog.Default,
    installedAppChecker: InstalledAppChecker = NoInstalledAppChecker,
): AboutAppAction {
    val network = catalog.getByKey(networkName)
        ?: SocialNetworkPlatform.getByName(networkName)?.toSpec()
        ?: SocialNetworkSpec(
            key = networkName,
            displayName = networkName,
            webUrlPrefix = "",
            appUrlPrefix = null,
        )
    return resolveSocialLinkAction(
        network = network,
        value = value,
        installedAppChecker = installedAppChecker,
    )
}

fun resolveSocialLinkAction(
    network: SocialNetworkSpec,
    value: String,
    installedAppChecker: InstalledAppChecker = NoInstalledAppChecker,
): AboutAppAction {
    val trimmedValue = value.trim()
    if (network.kind == SocialLinkKind.Email) {
        return AboutAppAction.SendEmail(trimmedValue)
    }

    if (network.kind == SocialLinkKind.Store) {
        return AboutAppAction.OpenPlayStore(trimmedValue)
    }

    val prefix = if (
        !network.androidPackageName.isNullOrBlank() &&
        !network.appUrlPrefix.isNullOrBlank() &&
        installedAppChecker.isAppInstalled(network.androidPackageName)
    ) {
        network.appUrlPrefix
    } else {
        network.webUrlPrefix
    }

    val url = if (ExternalUriPolicy.isSupportedAbsoluteUri(trimmedValue)) {
        trimmedValue
    } else {
        prefix + trimmedValue
    }
    return AboutAppAction.OpenUrl(url)
}

fun resolveGooglePlayWebFallbackAction(
    packageName: String,
    policySource: SocialLinkPolicySource = AllowAllSocialLinkPolicySource,
): AboutAppAction? {
    if (!policySource.canOpenGooglePlayLinks()) return null
    return AboutAppAction.OpenUrl(
        SocialNetworkPlatform.GooglePlay.webUrlPrefix + packageName.trim(),
    )
}
