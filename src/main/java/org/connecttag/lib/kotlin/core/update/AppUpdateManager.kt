package org.connecttag.lib.kotlin.core.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URL

@Serializable
data class UpdateInfo(
    val latestVersionCode: Int,
    val latestVersionName: String,
    val updateUrl: String,
    val releaseNotes: String? = null,
    val isMandatory: Boolean = false
)

/**
 * Represents the various states of an update check process.
 */
sealed class AppUpdateState {
    data object Idle : AppUpdateState()
    data object Checking : AppUpdateState()
    data class UpdateAvailable(val info: UpdateInfo) : AppUpdateState()
    data object UpToDate : AppUpdateState()
    data class Error(val throwable: Throwable) : AppUpdateState()
}

class AppUpdateManager(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    private val _updateState = MutableStateFlow<AppUpdateState>(AppUpdateState.Idle)
    val updateState: Flow<AppUpdateState> = _updateState.asStateFlow()

    /**
     * Checks for updates from a remote JSON URL asynchronously.
     */
    suspend fun checkForUpdates(configUrl: String): UpdateInfo? = withContext(Dispatchers.IO) {
        _updateState.value = AppUpdateState.Checking
        try {
            val response = URL(configUrl).readText()
            val info = json.decodeFromString<UpdateInfo>(response)
            val currentVersionCode = getAppVersionCode()

            if (info.latestVersionCode > currentVersionCode) {
                _updateState.value = AppUpdateState.UpdateAvailable(info)
                info
            } else {
                _updateState.value = AppUpdateState.UpToDate
                null
            }
        } catch (e: Exception) {
            _updateState.value = AppUpdateState.Error(e)
            null
        }
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

    fun openUpdateUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
