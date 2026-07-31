package org.connecttag.lib.kotlin.core.notifications

import androidx.lifecycle.viewModelScope
import org.connecttag.lib.kotlin.core.mvi.BaseMviViewModel
import org.connecttag.lib.kotlin.core.mvi.MviAction
import org.connecttag.lib.kotlin.core.mvi.MviState
import org.connecttag.lib.kotlin.core.mvi.NoMviEffect
import kotlinx.coroutines.launch
import org.connecttag.lib.kotlin.core.coroutine.DefaultDispatcherProvider

class NotificationDetailViewModel(
    private val inboxRepository: NotificationInboxRepository,
    private val uiMapper: ServerNotificationUiMapper,
) : BaseMviViewModel<NotificationDetailUiState, NotificationDetailAction, NoMviEffect>(
    initialState = NotificationDetailUiState(),
    reducer = { state, action ->
        when (action) {
            NotificationDetailAction.Loading -> state.copy(isLoading = true, error = null)
            is NotificationDetailAction.Loaded -> state.copy(isLoading = false, notification = action.notification)
            is NotificationDetailAction.Error -> state.copy(isLoading = false, error = action.message)
        }
    }
) {
    fun loadNotification(id: String) {
        dispatch(NotificationDetailAction.Loading)
        viewModelScope.launch(DefaultDispatcherProvider.io) {
            inboxRepository.fetchInboxNotification(id).fold(
                onSuccess = { response ->
                    dispatch(NotificationDetailAction.Loaded(uiMapper.map(response, isRuntimeInbox = true)))
                },
                onFailure = {
                    dispatch(NotificationDetailAction.Error(it.message ?: "Failed to load notification"))
                }
            )
        }
    }
}

data class NotificationDetailUiState(
    val isLoading: Boolean = false,
    val notification: ServerNotificationUiItem? = null,
    val error: String? = null,
) : MviState

sealed interface NotificationDetailAction : MviAction {
    data object Loading : NotificationDetailAction
    data class Loaded(val notification: ServerNotificationUiItem) : NotificationDetailAction
    data class Error(val message: String) : NotificationDetailAction
}
