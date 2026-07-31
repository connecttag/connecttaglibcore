package org.connecttag.lib.kotlin.core.update

import org.connecttag.lib.kotlin.core.utils.MviEffect
import org.connecttag.lib.kotlin.core.utils.MviIntent
import org.connecttag.lib.kotlin.core.utils.MviState

data class UpdateUiState(
    val updateState: AppUpdateState = AppUpdateState.Idle,
    val isChecking: Boolean = false,
    val updateInfo: UpdateInfo? = null,
    val error: Throwable? = null
) : MviState

sealed interface UpdateUiIntent : MviIntent {
    data class CheckForUpdates(val url: String) : UpdateUiIntent
    data object DismissUpdate : UpdateUiIntent
    data class StartUpdate(val url: String) : UpdateUiIntent
}

sealed interface UpdateUiEffect : MviEffect {
    data class OpenBrowser(val url: String) : UpdateUiEffect
    data object CloseOverlay : UpdateUiEffect
}
