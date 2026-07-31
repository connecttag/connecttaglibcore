package org.connecttag.lib.kotlin.core.uimodel

import org.connecttag.lib.kotlin.core.error.AppError
import org.connecttag.lib.kotlin.core.text.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * A standard wrapper for screen states.
 */
data class PageState<out T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val error: UiText? = null
) {
    val hasData: Boolean get() = data != null
    val hasError: Boolean get() = error != null

    companion object {
        fun <T> loading(): PageState<T> = PageState(isLoading = true)
        fun <T> success(data: T): PageState<T> = PageState(data = data)
        fun <T> error(uiText: UiText): PageState<T> = PageState(error = uiText)

        fun error(throwable: Throwable): PageState<Nothing> {
            val uiText = when (throwable) {
                is AppError -> throwable.message?.let { UiText.DynamicString(it) } ?: UiText.DynamicString(throwable.key)
                else -> throwable.message?.let { UiText.DynamicString(it) } ?: UiText.DynamicString(throwable.toString())
            }
            return PageState(error = uiText)
        }
    }
}

fun <T> Result<T>.asPageState(): PageState<T> {
    return fold(
        onSuccess = { PageState.success(it) },
        onFailure = { PageState.error(it) }
    )
}

fun <T> Flow<Result<T>>.asPageStateFlow(): Flow<PageState<T>> {
    return this.map { it.asPageState() }
        .onStart { emit(PageState.loading()) }
}
