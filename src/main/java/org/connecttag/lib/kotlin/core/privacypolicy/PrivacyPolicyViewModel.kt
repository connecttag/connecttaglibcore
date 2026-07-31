package org.connecttag.lib.kotlin.core.privacypolicy

import org.connecttag.lib.kotlin.core.mvi.BaseMviViewModel
import kotlinx.coroutines.Job

open class PrivacyPolicyViewModel(
    private val projectKey: String,
    private val contentProvider: PrivacyPolicyContentProvider,
    private val consentStore: PrivacyConsentStore,
) : BaseMviViewModel<PrivacyPolicyUiState, PrivacyPolicyUiEvent, PrivacyPolicyUiEffect>(
    initialState = PrivacyPolicyUiState(),
    reducer = { state, event ->
        // Note: The source project uses a BaseStateViewModel which has updateState.
        // I will implement the logic inside onAction for consistency with the source's flow
        // even if it bypasses the reducer for now, or I can define a dummy reducer.
        state
    }
) {

    private var lastLoadedConfig: PrivacyPolicyConfig? = null
    private var loadJob: Job? = null

    override fun onAction(action: PrivacyPolicyUiEvent) {
        when (action) {
            is PrivacyPolicyUiEvent.Load -> load(action.config)
            PrivacyPolicyUiEvent.Retry -> lastLoadedConfig?.let(::load)
            PrivacyPolicyUiEvent.Accept -> accept()
            PrivacyPolicyUiEvent.Decline,
            PrivacyPolicyUiEvent.NavigateBack -> sendEffect(PrivacyPolicyUiEffect.NavigateBack)
        }
    }

    private fun load(config: PrivacyPolicyConfig) {
        val stateSnapshot = currentState
        if (
            lastLoadedConfig == config &&
            !stateSnapshot.isLoading &&
            (!config.enabled || stateSnapshot.hasDisplayContent)
        ) {
            return
        }

        lastLoadedConfig = config
        loadJob?.cancel()
        loadJob = launch {
            updateState {
                it.copy(
                    isLoading = true,
                    enabled = config.enabled,
                    title = "",
                    type = PrivacyPolicyType.NONE,
                    url = null,
                    content = "",
                    documentKey = null,
                    isMandatory = false,
                    effectiveAt = null,
                    documents = emptyList(),
                )
            }

            if (!config.enabled) {
                updateState { it.copy(isLoading = false) }
                return@launch
            }

            val content = contentProvider.loadContent(projectKey, config.resourceKey).getOrElse {
                updateState { state -> state.copy(isLoading = false) }
                return@launch
            }
            if (!content.matches(config)) {
                updateState { state -> state.copy(isLoading = false) }
                return@launch
            }
            updateState {
                it.copy(
                    isLoading = false,
                    enabled = true,
                    title = content.title,
                    type = content.type,
                    url = content.url,
                    content = content.content,
                    documentKey = content.documentKey,
                    isMandatory = content.isMandatory,
                    effectiveAt = content.effectiveAt,
                    documents = content.documents,
                )
            }
        }
    }

    private fun accept() {
        val documentKeys = currentState.requiredDocumentKeys
        launch {
            documentKeys.forEach { documentKey ->
                consentStore.acceptConsent(documentKey)
            }
            sendEffect(PrivacyPolicyUiEffect.Accepted)
        }
    }

    private fun PrivacyPolicyDisplayContent.matches(config: PrivacyPolicyConfig): Boolean {
        return !config.requiresConsent || documents
            .filter(LegalPolicyDisplayDocument::isMandatory)
            .all { document -> document.documentKey != null && document.hasDisplayContent }
            .takeIf { documents.any(LegalPolicyDisplayDocument::isMandatory) }
            ?: (isMandatory && documentKey != null)
    }
}
