package org.connecttag.lib.kotlin.core.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.*

interface PermissionService {
    fun checkStatus(permission: PlatformPermission): PlatformPermissionStatus
    fun openSettings()
}

class AndroidPermissionService(private val context: Context) : PermissionService {
    override fun checkStatus(permission: PlatformPermission): PlatformPermissionStatus {
        val manifestPermissions = when (permission) {
            PlatformPermission.CAMERA -> listOf(android.Manifest.permission.CAMERA)
            PlatformPermission.LOCATION -> listOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            PlatformPermission.REMOTE_NOTIFICATIONS -> if (android.os.Build.VERSION.SDK_INT >= 33) {
                listOf(android.Manifest.permission.POST_NOTIFICATIONS)
            } else emptyList()
            else -> emptyList()
        }

        if (manifestPermissions.isEmpty()) return PlatformPermissionStatus.NotRequired

        val allGranted = manifestPermissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        return if (allGranted) PlatformPermissionStatus.Granted else PlatformPermissionStatus.Denied
    }

    override fun openSettings() {
        // Intent to open settings
    }
}

class PermissionManager(
    private val service: PermissionService
) {
    private val _permissionsState = MutableStateFlow<Map<PlatformPermission, PlatformPermissionStatus>>(emptyMap())
    val permissionsState: StateFlow<Map<PlatformPermission, PlatformPermissionStatus>> = _permissionsState.asStateFlow()

    fun refresh(permission: PlatformPermission) {
        val status = service.checkStatus(permission)
        _permissionsState.update { it + (permission to status) }
    }

    fun isGranted(permission: PlatformPermission): Boolean {
        return service.checkStatus(permission).isGrantedOrNotRequired()
    }
}
