package org.connecttag.lib.kotlin.core.permissions

enum class PlatformPermissionNextAction {
    Proceed,
    ExplainThenRequest,
    Request,
    OpenSettings,
    WaitForCooldown,
    Unavailable,
}

data class PlatformPermissionRationaleDecision(
    val action: PlatformPermissionNextAction,
    val retryAfterMillis: Long? = null,
) {
    val requiresExplanation: Boolean
        get() = action == PlatformPermissionNextAction.ExplainThenRequest

    val canRequest: Boolean
        get() = action == PlatformPermissionNextAction.Request ||
            action == PlatformPermissionNextAction.ExplainThenRequest

    val canOpenSettings: Boolean
        get() = action == PlatformPermissionNextAction.OpenSettings
}

data class PermissionRationalePolicy(
    val explainBeforeFirstRequest: Boolean = true,
    val explainAfterDenial: Boolean = true,
    val denialCooldownMillis: Long = 30_000L,
) {
    fun evaluate(
        status: PlatformPermissionStatus,
        requestCount: Int,
        lastDeniedAtMillis: Long?,
        nowMillis: Long,
    ): PlatformPermissionRationaleDecision {
        return when (status) {
            PlatformPermissionStatus.Granted,
            PlatformPermissionStatus.NotRequired -> {
                PlatformPermissionRationaleDecision(PlatformPermissionNextAction.Proceed)
            }
            PlatformPermissionStatus.PermanentlyDenied -> {
                PlatformPermissionRationaleDecision(PlatformPermissionNextAction.OpenSettings)
            }
            PlatformPermissionStatus.Unavailable -> {
                PlatformPermissionRationaleDecision(PlatformPermissionNextAction.Unavailable)
            }
            PlatformPermissionStatus.NotDetermined -> {
                PlatformPermissionRationaleDecision(
                    if (explainBeforeFirstRequest && requestCount == 0) {
                        PlatformPermissionNextAction.ExplainThenRequest
                    } else {
                        PlatformPermissionNextAction.Request
                    },
                )
            }
            PlatformPermissionStatus.Denied -> {
                val deniedAt = lastDeniedAtMillis ?: 0L
                val elapsed = nowMillis - deniedAt
                if (elapsed < denialCooldownMillis) {
                    PlatformPermissionRationaleDecision(
                        action = PlatformPermissionNextAction.WaitForCooldown,
                        retryAfterMillis = denialCooldownMillis - elapsed,
                    )
                } else {
                    PlatformPermissionRationaleDecision(
                        if (explainAfterDenial) {
                            PlatformPermissionNextAction.ExplainThenRequest
                        } else {
                            PlatformPermissionNextAction.Request
                        },
                    )
                }
            }
        }
    }
}
