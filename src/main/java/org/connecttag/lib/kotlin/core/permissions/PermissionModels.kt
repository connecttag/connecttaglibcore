package org.connecttag.lib.kotlin.core.permissions

enum class PlatformPermission {
    CAMERA,
    GALLERY,
    LOCATION,
    COARSE_LOCATION,
    BACKGROUND_LOCATION,
    MICROPHONE,
    REMOTE_NOTIFICATIONS,
}

enum class PlatformPermissionStatus {
    Granted,
    Denied,
    PermanentlyDenied,
    NotDetermined,
    NotRequired,
    Unavailable,
}

enum class PlatformPermissionUnavailableReason {
    RuntimeNotInstalled,
    UnsupportedPlatform,
    ProviderFailure,
    TimedOut,
}

fun PlatformPermissionStatus.isGrantedOrNotRequired(): Boolean {
    return this == PlatformPermissionStatus.Granted ||
        this == PlatformPermissionStatus.NotRequired
}

data class PermissionRequestResult(
    val permission: PlatformPermission,
    val status: PlatformPermissionStatus,
    val unavailableReason: PlatformPermissionUnavailableReason? = null,
) {
    init {
        require(
            unavailableReason == null || status == PlatformPermissionStatus.Unavailable,
        ) {
            "An unavailable reason requires PlatformPermissionStatus.Unavailable."
        }
    }
}
