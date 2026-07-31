package org.connecttag.lib.kotlin.core.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.connecttag.lib.kotlin.core.R
import org.connecttag.lib.kotlin.core.ui.components.scaffold.AkScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit,
    sheetState: SheetState,
) {
    AkScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(SettingsUiEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            FloatingSaveBar(
                isVisible = uiState.hasUnsavedChanges,
                isSaving = uiState.isSaving,
                onSave = { onEvent(SettingsUiEvent.SaveChanges) },
                onDiscard = { onEvent(SettingsUiEvent.DiscardChanges) }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(uiState.sections, key = { it.title }) { section ->
                    RenderSettingSection(
                        section = section,
                        onToggleChanged = { key, checked ->
                            onEvent(SettingsUiEvent.ToggleChanged(key, checked))
                        },
                        onChoiceClicked = { item ->
                            onEvent(SettingsUiEvent.ChoiceClicked(item))
                        },
                        onClick = { key ->
                            // Handle other clicks if needed
                        }
                    )
                }
            }

            uiState.activeSelection?.let { choiceItem ->
                SelectionBottomSheet(
                    sheetState = sheetState,
                    title = choiceItem.title,
                    icon = choiceItem.icon,
                    options = choiceItem.options,
                    selectedValue = choiceItem.selectedOption,
                    onValueChange = { newValue ->
                        onEvent(SettingsUiEvent.ChoiceChanged(choiceItem.key, newValue))
                    },
                    onDismiss = {
                        onEvent(SettingsUiEvent.DismissSelection)
                    }
                )
            }
        }
    }
}
