package org.connecttag.lib.kotlin.core.utils

/**
 * Represents the state of a page or data fetch operation.
 */
data class PageState<out T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
) {
    companion object {
        fun <T> idle() = PageState<T>()
        fun <T> loading() = PageState<T>(isLoading = true)
        fun <T> success(data: T) = PageState(data = data)
        fun <T> failure(error: Throwable) = PageState<T>(error = error)
    }
}
