package org.connecttag.lib.kotlin.core.privacypolicy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.connecttag.lib.kotlin.core.actions.PlatformLinkOpener

@Composable
fun PrivacyPolicyRoute(
    config: PrivacyPolicyConfig,
    viewModel: PrivacyPolicyViewModel,
    linkOpener: PlatformLinkOpener,
    onBack: () -> Unit,
    onAccepted: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(config) {
        viewModel.onAction(PrivacyPolicyUiEvent.Load(config))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                PrivacyPolicyUiEffect.Accepted -> onAccepted()
                PrivacyPolicyUiEffect.NavigateBack -> onBack()
            }
        }
    }

    PrivacyPolicyScreen(
        state = state,
        onBack = { viewModel.onAction(PrivacyPolicyUiEvent.NavigateBack) },
        linkOpener = linkOpener,
        onRetry = { viewModel.onAction(PrivacyPolicyUiEvent.Retry) },
        onAccept = { viewModel.onAction(PrivacyPolicyUiEvent.Accept) },
        onDecline = { viewModel.onAction(PrivacyPolicyUiEvent.Decline) },
        allowDismiss = true, // Or based on config
    )
}
