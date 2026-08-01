package org.connecttag.lib.kotlin.core.error

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.WarningAmber
import org.connecttag.lib.kotlin.core.R
import org.connecttag.lib.kotlin.core.text.UiText
import org.connecttag.lib.kotlin.core.uimodel.UiState
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Global error handler to map exceptions to UI states.
 */
object AppErrorHandler {

    /**
     * Maps a [Throwable] to a [UiState.Error].
     */
    fun map(throwable: Throwable): UiState.Error {
        return when (throwable) {
            is UnknownHostException -> UiState.Error(
                message = UiText.StringResource(R.string.no_internet_connection),
                icon = Icons.Default.CloudOff
            )
            is SocketTimeoutException -> UiState.Error(
                message = UiText.StringResource(R.string.error_timeout),
                icon = Icons.Default.WarningAmber
            )
            is IOException -> UiState.Error(
                message = UiText.StringResource(R.string.error_io),
                icon = Icons.Default.ErrorOutline
            )
            else -> UiState.Error(
                message = UiText.StringResource(R.string.error_unknown),
                icon = Icons.Default.ErrorOutline
            )
        }
    }
}
