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
}
