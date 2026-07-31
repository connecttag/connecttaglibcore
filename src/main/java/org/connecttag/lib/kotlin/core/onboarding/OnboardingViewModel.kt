package org.connecttag.lib.kotlin.core.onboarding

import org.connecttag.lib.kotlin.core.mvi.BaseMviViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first

open class OnboardingViewModel(
    private val projectKey: String,
    private val surface: String,
    private val appVersionCode: Int,
    private val pageProvider: OnboardingPageProvider,
    private val progressStore: OnboardingProgressStore,
) : BaseMviViewModel<OnboardingUiState, OnboardingUiEvent, OnboardingUiEffect>(
    initialState = OnboardingUiState(),
    reducer = { state, event ->
        // Logic handled in onAction for complexity
        state
    }
) {

    private var lastLoadedConfig: OnboardingConfig? = null
    private var loadJob: Job? = null
    private var loadedCampaign: OnboardingCampaign? = null

    override fun onAction(action: OnboardingUiEvent) {
        when (action) {
            is OnboardingUiEvent.Load -> load(action.config)
            OnboardingUiEvent.Skip -> {
                if (!currentState.isLoading && currentState.allowSkip) {
                    completeOnboarding(executeCampaignAction = false)
                }
            }
            OnboardingUiEvent.Finish -> {
                if (!currentState.isLoading && loadedCampaign != null) {
                    completeOnboarding(executeCampaignAction = true)
                }
            }
        }
    }

    private fun load(config: OnboardingConfig) {
        val stateSnapshot = currentState
        if (
            lastLoadedConfig == config &&
            !stateSnapshot.isLoading &&
            stateSnapshot.pages.isNotEmpty()
        ) {
            return
        }

        lastLoadedConfig = config
        loadJob?.cancel()
        loadJob = launch {
            if (!config.enabled) {
                sendEffect(OnboardingUiEffect.NavigateHome)
                return@launch
            }

            updateState {
                it.copy(
                    isLoading = true,
                )
            }

            val campaign = pageProvider
                .loadCampaign(projectKey = projectKey, surface = surface, appVersionCode = appVersionCode)
                .getOrNull()
            
            if (campaign == null) {
                sendEffect(OnboardingUiEffect.NavigateHome)
                return@launch
            }
            
            val completions = progressStore.completions().first()
            if (!OnboardingCampaignDisplayPolicy.shouldShow(campaign, completions)) {
                sendEffect(OnboardingUiEffect.NavigateHome)
                return@launch
            }
            
            val pages = campaign.pages

            if (pages.isEmpty()) {
                updateState { it.copy(isLoading = false, pages = emptyList()) }
                sendEffect(OnboardingUiEffect.NavigateHome)
                return@launch
            }

            updateState {
                it.copy(
                    isLoading = false,
                    pages = pages,
                    allowSkip = campaign.allowSkip,
                    finishLabel = campaign.ctaLabel,
                )
            }
            loadedCampaign = campaign
        }
    }

    private fun completeOnboarding(executeCampaignAction: Boolean) {
        launch {
            val campaign = loadedCampaign
            campaign?.let { current ->
                progressStore.markCompleted(
                    OnboardingCampaignKey(
                        projectId = current.projectId,
                        campaignId = current.campaignId,
                        campaignVersion = current.version,
                    ),
                )
            }
            if (executeCampaignAction && campaign != null) {
                sendEffect(OnboardingUiEffect.ExecuteCampaignAction(campaign.ctaAction))
            } else {
                sendEffect(OnboardingUiEffect.NavigateHome)
            }
        }
    }
}
