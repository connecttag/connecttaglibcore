package org.connecttag.lib.kotlin.core.ui.components.state

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.connecttag.lib.kotlin.core.result.PlatformResult

/**
 * A container that handles the display logic for [PlatformResult].
 */
@Composable
fun <T> AppResponseContainer(
    result: PlatformResult<T>,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    loadingMessage: String? = null,
    emptyMessage: String = "لا توجد بيانات لعرضها حالياً",
    content: @Composable (T) -> Unit
) {
    when (result) {
        is PlatformResult.Loading -> {
            LoadingView(modifier = modifier, message = loadingMessage)
        }
        is PlatformResult.Success -> {
            content(result.data)
        }
        is PlatformResult.Failure -> {
            ErrorView(
                modifier = modifier,
                message = result.error.message ?: result.error.key,
                onRetry = onRetry
            )
        }
        is PlatformResult.Empty -> {
            EmptyView(modifier = modifier, message = emptyMessage)
        }
        is PlatformResult.Idle -> {
            // Usually we show nothing or the initial state
        }
    }
}
