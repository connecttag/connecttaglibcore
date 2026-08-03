package org.connecttag.lib.kotlin.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.connecttag.lib.kotlin.core.network.connectivity.ConnectivityMonitor
import org.connecttag.lib.kotlin.core.update.AppStatus
import org.connecttag.lib.kotlin.core.update.AppStatusManager
import org.connecttag.lib.kotlin.core.update.AppStatusResult
import org.connecttag.lib.kotlin.core.update.UpdateOverlay
import org.connecttag.lib.kotlin.core.update.UpdateInfo
import org.connecttag.lib.kotlin.core.utils.openBrowser

/**
 * A top-level wrapper that automatically handles App Status (Update/Maintenance)
 * and Connectivity status banners.
 */
@Composable
fun AppConnectTagScreen(
    appStatusManager: AppStatusManager,
    connectivityMonitor: ConnectivityMonitor,
    modifier: Modifier = Modifier,
    showConnectivityBanner: Boolean = true,
    onOptionalUpdateDismiss: () -> Unit = {},
    content: @Composable (isConnected: Boolean, appStatus: AppStatusResult) -> Unit
) {
    val context = LocalContext.current
    val isConnected by connectivityMonitor.isConnected.collectAsState(initial = true)
    val appStatusResult by appStatusManager.appStatus.collectAsState(initial = AppStatusResult(AppStatus.NORMAL))

    var showOptionalUpdate by remember { mutableStateOf(true) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. Live Connectivity Banner
            if (showConnectivityBanner) {
                AnimatedVisibility(
                    visible = !isConnected,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    ConnectivityWarningBanner()
                }
            }

            // 2. Main Content
            Box(modifier = Modifier.weight(1f)) {
                content(isConnected, appStatusResult)
            }
        }

        // 3. Global Overlays (Maintenance / Update)
        when (appStatusResult.status) {
            AppStatus.FORCE_UPDATE -> {
                UpdateOverlay(
                    info = UpdateInfo(
                        latestVersionCode = 0,
                        latestVersionName = "Required",
                        updateUrl = appStatusResult.updateUrl ?: "",
                        releaseNotes = appStatusResult.message,
                        isMandatory = true
                    ),
                    onDismiss = { },
                    onUpdateClick = { url -> context.openBrowser(url) }
                )
            }
            AppStatus.MAINTENANCE -> {
                 UpdateOverlay(
                    info = UpdateInfo(
                        latestVersionCode = 0,
                        latestVersionName = appStatusResult.title ?: "Maintenance",
                        updateUrl = "",
                        releaseNotes = appStatusResult.message ?: "The app is currently under maintenance. Please check back later.",
                        isMandatory = true
                    ),
                    onDismiss = { },
                    onUpdateClick = { }
                )
            }
            AppStatus.OPTIONAL_UPDATE -> {
                if (showOptionalUpdate) {
                    UpdateOverlay(
                        info = UpdateInfo(
                            latestVersionCode = 0,
                            latestVersionName = "Available",
                            updateUrl = appStatusResult.updateUrl ?: "",
                            releaseNotes = appStatusResult.message,
                            isMandatory = false
                        ),
                        onDismiss = { 
                            showOptionalUpdate = false
                            onOptionalUpdateDismiss()
                        },
                        onUpdateClick = { url -> context.openBrowser(url) }
                    )
                }
            }
            else -> { /* Normal - don't block the screen */ }
        }
    }
}
