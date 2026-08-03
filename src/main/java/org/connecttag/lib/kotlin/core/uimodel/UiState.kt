package org.connecttag.lib.kotlin.core.uimodel

import androidx.compose.ui.graphics.vector.ImageVector
import org.connecttag.lib.kotlin.core.text.UiText

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    
    data class Success<T>(val data: T) : UiState<T>()
    
    data class Error(
        val message: UiText,
        val icon: ImageVector? = null,
        val actionText: UiText? = null
    ) : UiState<Nothing>()

    fun getOrNull(): T? = (this as? Success)?.data
}

/**
 * Extension function to convert UiState to PageState for BaseScreen integration.
 */
fun <T> UiState<T>.toPageState(): PageState<T> {
    return when (this) {
        is UiState.Loading -> PageState.loading()
        is UiState.Success -> PageState.success(this.data)
        is UiState.Error -> PageState.error(this.message)
    }
}

/**
 * Extension function to convert UiState to PageState with a custom data object.
 * Useful when the screen state contains more than just the result of the UiState.
 */
fun <T, R> UiState<T>.toPageState(data: R): PageState<R> {
    return when (this) {
        is UiState.Loading -> PageState.loading()
        is UiState.Success -> PageState.success(data)
        is UiState.Error -> PageState.error(this.message)
    }
}
