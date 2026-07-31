package org.connecttag.lib.kotlin.core.onboarding

import org.connecttag.lib.kotlin.core.mvi.MviAction
import org.connecttag.lib.kotlin.core.mvi.MviEffect
import org.connecttag.lib.kotlin.core.mvi.MviState
import kotlinx.coroutines.flow.Flow
import org.connecttag.lib.kotlin.core.actions.ActionType

data class OnboardingConfig(
    val enabled: Boolean,
)

enum class OnboardingShowPolicy {
    FIRST_RUN,
    ONCE_PER_VERSION,
    ALWAYS,
}

data class OnboardingCampaign(
    val projectId: String,
    val campaignId: String,
    val version: String,
    val showPolicy: OnboardingShowPolicy,
    val allowSkip: Boolean,
    val ctaLabel: String,
    val ctaAction: OnboardingCampaignAction,
    val pages: List<OnboardingPage>,
)

data class OnboardingCampaignAction(
    val type: ActionType,
    val content: String,
)

data class OnboardingPage(
    val title: String,
    val description: String,
    val stepNumber: Int,
    val assetKey: String? = null,
    val image: String? = null,
    val imageAltText: String? = null,
)

data class OnboardingUiState(
    val isLoading: Boolean = true,
    val pages: List<OnboardingPage> = emptyList(),
    val allowSkip: Boolean = true,
    val finishLabel: String? = null,
) : MviState

sealed interface OnboardingUiEvent : MviAction {
    data class Load(
        val config: OnboardingConfig,
    ) : OnboardingUiEvent

    data object Skip : OnboardingUiEvent
    data object Finish : OnboardingUiEvent
}

sealed interface OnboardingUiEffect : MviEffect {
    data object NavigateHome : OnboardingUiEffect
    data class ExecuteCampaignAction(val action: OnboardingCampaignAction) : OnboardingUiEffect
}

interface OnboardingPageProvider {
    suspend fun loadCampaign(
        projectKey: String,
        surface: String,
        appVersionCode: Int,
    ): Result<OnboardingCampaign?>
}

interface OnboardingProgressStore {
    fun completions(): Flow<List<OnboardingCompletion>>
    suspend fun markCompleted(key: OnboardingCampaignKey)
}

data class OnboardingCampaignKey(
    val projectId: String,
    val campaignId: String,
    val campaignVersion: String,
)

data class OnboardingCompletion(
    val key: OnboardingCampaignKey,
    val completedAtEpochMillis: Long,
)
