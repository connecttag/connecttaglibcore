package org.connecttag.lib.kotlin.core.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.connecttag.lib.kotlin.core.actions.ActionRequestHandler
import org.connecttag.lib.kotlin.core.actions.ActionRequestFactory

@Composable
fun OnboardingRoute(
    config: OnboardingConfig,
    viewModel: OnboardingViewModel,
    onNavigateHome: () -> Unit,
    actionHandler: ActionRequestHandler,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(config) {
        viewModel.onAction(OnboardingUiEvent.Load(config))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                OnboardingUiEffect.NavigateHome -> onNavigateHome()
                is OnboardingUiEffect.ExecuteCampaignAction -> {
                    ActionRequestFactory.from(
                        effect.action.type,
                        effect.action.content
                    )?.let { actionHandler(it) }
                    onNavigateHome()
                }
            }
        }
    }

    OnboardingContent(
        state = state,
        onSkip = { viewModel.onAction(OnboardingUiEvent.Skip) },
        onFinish = { viewModel.onAction(OnboardingUiEvent.Finish) },
    )
}
