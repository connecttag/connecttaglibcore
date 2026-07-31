package org.connecttag.lib.kotlin.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.connecttag.lib.kotlin.core.coroutine.DefaultDispatcherProvider
import org.connecttag.lib.kotlin.core.coroutine.DispatcherProvider
import org.connecttag.lib.kotlin.core.uimodel.PageState
import org.connecttag.lib.kotlin.core.uimodel.asPageStateFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Shared ViewModel adapter for ConnectTag MVI.
 */
abstract class BaseMviViewModel<State : MviState, Action : MviAction, Effect : MviEffect>(
    initialState: State,
    reducer: (State, Action) -> State,
    middleware: List<MviMiddleware<State, Action, Effect>> = emptyList(),
    validators: List<ActionValidator<Action>> = emptyList(),
    validationFailureEffect: ((ActionValidationResult.Rejected, Action) -> Effect?)? = null,
    actionPolicyResolver: ActionPolicyResolver<Action> = ActionPolicyResolver {
        ActionPolicy()
    },
    config: MviStoreConfig = MviStoreConfig(),
    protected val dispatchers: DispatcherProvider = DefaultDispatcherProvider,
    logger: MviLogger = MviLogger.None,
    errorHandler: MviStoreErrorHandler = MviStoreErrorHandler.None,
) : ViewModel(), MviContainer<State, Action, Effect> {

    private val store = MviStore(
        initialState = initialState,
        scope = viewModelScope,
        reducer = reducer,
        middleware = middleware,
        validators = validators,
        validationFailureEffect = validationFailureEffect,
        actionPolicyResolver = actionPolicyResolver,
        config = config,
        logger = logger,
        errorHandler = errorHandler,
    )

    override val state: StateFlow<State> = store.state
    val uiState: StateFlow<State> = state

    override val effect: Flow<Effect> = store.effect
    val uiEffect: Flow<Effect> = effect

    protected val currentState: State get() = state.value

    override fun onAction(action: Action) {
        store.onAction(action)
    }

    protected fun dispatch(action: Action): Boolean = store.dispatch(action)

    protected fun sendEffect(effect: Effect): Boolean = store.emitEffect(effect)

    protected fun launch(
        dispatcher: CoroutineDispatcher = dispatchers.main,
        block: suspend () -> Unit,
    ): Job = viewModelScope.launch(dispatcher) {
        block()
    }

    protected fun launchMain(block: suspend () -> Unit): Job = launch(dispatchers.main, block)
    protected fun launchIo(block: suspend () -> Unit): Job = launch(dispatchers.io, block)
    protected fun launchDefault(block: suspend () -> Unit): Job = launch(dispatchers.default, block)

    protected fun <T> loadPage(
        flow: Flow<Result<T>>,
        onResult: (PageState<T>) -> Unit,
    ) {
        launch {
            flow.asPageStateFlow().collect { onResult(it) }
        }
    }

    protected fun updateState(reduce: (State) -> State) {
        store.reduceState(reduce)
    }
}
