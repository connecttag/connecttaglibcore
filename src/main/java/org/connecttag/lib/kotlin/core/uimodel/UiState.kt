package org.connecttag.lib.kotlin.core.uimodel

import org.connecttag.lib.kotlin.core.text.UiText

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val uiText: UiText) : UiState<Nothing>()
}
