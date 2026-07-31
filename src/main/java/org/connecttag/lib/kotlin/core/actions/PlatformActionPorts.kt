package org.connecttag.lib.kotlin.core.actions

enum class PlatformLinkTarget {
    External,
    InApp,
}

data class PlatformLinkRequest(
    val url: String,
    val target: PlatformLinkTarget = PlatformLinkTarget.External,
    val title: String? = null,
)

data class PlatformShareRequest(
    val text: String,
    val title: String? = null,
    val url: String? = null,
)

sealed interface PlatformActionOutcome {
    data object Completed : PlatformActionOutcome
    data object Unsupported : PlatformActionOutcome
    data object Blocked : PlatformActionOutcome
    data class Failed(val cause: Throwable? = null) : PlatformActionOutcome
}

/** Portable boundary for opening links. Platform Intent/URI types stay in adapters. */
fun interface PlatformLinkOpener {
    fun open(request: PlatformLinkRequest): PlatformActionOutcome
}

/** Portable boundary for invoking the platform share surface. */
fun interface PlatformShareService {
    fun share(request: PlatformShareRequest): PlatformActionOutcome
}

interface PlatformExternalActions : PlatformLinkOpener, PlatformShareService
