package org.connecttag.lib.kotlin.core.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import org.connecttag.lib.kotlin.core.actions.ActionRequestHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit = {}
) {
    val uiState by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showResetDialog by remember { mutableStateOf(false) }
    var showClearCacheDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SettingsUiEffect.NavigateBack -> onBack()
                is SettingsUiEffect.ShowToast -> onShowMessage(effect.message)
                SettingsUiEffect.ShowResetDialog -> showResetDialog = true
                SettingsUiEffect.ShowClearCacheDialog -> showClearCacheDialog = true
            }
        }
    }

    if (showResetDialog) {
        ResetSettingsDialog(
            onConfirm = {
                showResetDialog = false
                viewModel.onAction(SettingsUiEvent.ResetToDefaults)
            },
            onDismiss = { showResetDialog = false }
        )
    }

    if (showClearCacheDialog) {
        ClearCacheDialog(
            onConfirm = {
                showClearCacheDialog = false
                // Handle clear cache
            },
            onDismiss = { showClearCacheDialog = false }
        )
    }

    SettingsScreen(
        uiState = uiState,
        onEvent = viewModel::onAction,
        sheetState = sheetState
    )
}
