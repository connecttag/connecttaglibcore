package org.connecttag.lib.kotlin.core.update

import kotlinx.serialization.Serializable

@Serializable
data class AppStatusInfo(
    val maintenanceMode: Boolean = false,
    val maintenanceMessage: String? = null,
    val minVersionCode: Int = 0,
    val latestVersionCode: Int = 0,
    val updateUrl: String? = null,
    val updateTitle: String? = null,
    val updateMessage: String? = null,
    val forceUpdate: Boolean = false
)

enum class AppStatus {
    NORMAL,
    MAINTENANCE,
    FORCE_UPDATE,
    OPTIONAL_UPDATE
}

data class AppStatusResult(
    val status: AppStatus,
    val message: String? = null,
    val updateUrl: String? = null,
    val title: String? = null
)
