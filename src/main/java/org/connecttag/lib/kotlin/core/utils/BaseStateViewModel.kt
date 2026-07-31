package org.connecttag.lib.kotlin.core.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * A base ViewModel that implements a simple MVI-like state management.
 * Provides standard dispatchers and launch helpers.
 */
abstract class BaseStateViewModel<State : MviState, Effect : MviEffect>(
    initialState: State,
    protected val dispatchers: DispatcherProvider = DefaultDispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()
    val uiState: StateFlow<State> = state

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect: Flow<Effect> = _effect.receiveAsFlow()
    val uiEffect: Flow<Effect> = effect

    protected val currentState: State
        get() = _state.value

    /**
     * Updates the current state using the provided reducer.
     */
    protected fun updateState(reducer: (State) -> State) {
        _state.update(reducer)
    }

    /**
     * Sends a side effect to the UI.
     */
    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch(dispatchers.main) {
            _effect.send(effect)
        }
    }

    /**
     * Helper to launch coroutines in the viewModelScope.
     */
    protected fun launch(
        dispatcher: CoroutineDispatcher = dispatchers.main,
        block: suspend () -> Unit
    ): Job = viewModelScope.launch(dispatcher) {
        block()
    }

    protected fun launchMain(block: suspend () -> Unit): Job = launch(dispatchers.main, block)
    protected fun launchIo(block: suspend () -> Unit): Job = launch(dispatchers.io, block)
    protected fun launchDefault(block: suspend () -> Unit): Job = launch(dispatchers.default, block)

    /**
     * Validates a field and returns true if successful.
     */
    protected fun <T> validateField(
        value: T,
        validator: Validator<T>,
        onError: (Int) -> Unit = {}
    ): Boolean {
        return when (val result = validator.validate(value)) {
            is ValidationResult.Success -> true
            is ValidationResult.Failure -> {
                onError(result.messageKey)
                false
            }
        }
    }
}
