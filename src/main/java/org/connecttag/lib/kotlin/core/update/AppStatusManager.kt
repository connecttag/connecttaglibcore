package org.connecttag.lib.kotlin.core.update

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Main manager to determine the application's operational status (Normal, Maintenance, or Update required).
 */
class AppStatusManager(
    private val context: Context,
    private val statusProvider: AppStatusProvider
) {

    /**
     * Observes the current application status as a [Flow].
     */
    val appStatus: Flow<AppStatusResult> = statusProvider.getAppStatusFlow().map { info ->
        calculateStatus(info)
    }

    /**
     * Checks the current status synchronously.
     */
    fun checkStatus(): AppStatusResult {
        return calculateStatus(statusProvider.getAppStatusSync())
    }

    private fun calculateStatus(info: AppStatusInfo?): AppStatusResult {
        if (info == null) return AppStatusResult(AppStatus.NORMAL)

        // 1. Check Maintenance Mode
        if (info.maintenanceMode) {
            return AppStatusResult(
                status = AppStatus.MAINTENANCE,
                message = info.maintenanceMessage,
                title = "Under Maintenance"
            )
        }

        val currentVersionCode = getAppVersionCode()

        // 2. Check Force Update
        if (currentVersionCode < info.minVersionCode || (info.forceUpdate && currentVersionCode < info.latestVersionCode)) {
            return AppStatusResult(
                status = AppStatus.FORCE_UPDATE,
                message = info.updateMessage,
                updateUrl = info.updateUrl,
                title = info.updateTitle ?: "Update Required"
            )
        }

        // 3. Check Optional Update
        if (currentVersionCode < info.latestVersionCode) {
            return AppStatusResult(
                status = AppStatus.OPTIONAL_UPDATE,
                message = info.updateMessage,
                updateUrl = info.updateUrl,
                title = info.updateTitle ?: "New Version Available"
            )
        }

        return AppStatusResult(AppStatus.NORMAL)
    }

    private fun getAppVersionCode(): Int {
        return try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                pInfo.longVersionCode.toInt()
            } else {
                @Suppress("DEPRECATION")
                pInfo.versionCode
            }
        } catch (e: Exception) {
            0
        }
    }
}
