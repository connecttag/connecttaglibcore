package org.connecttag.lib.kotlin.core.settings

import org.connecttag.lib.kotlin.core.mvi.BaseMviViewModel

open class SettingsViewModel(
    private val initialSections: List<SettingSection>
) : BaseMviViewModel<SettingsUiState, SettingsUiEvent, SettingsUiEffect>(
    initialState = SettingsUiState(sections = initialSections),
    reducer = { state, event ->
        when (event) {
            is SettingsUiEvent.ToggleChanged -> {
                val updatedSections = state.sections.map { section ->
                    section.copy(items = section.items.map { item ->
                        if (item is SettingItem.Toggle && item.key == event.key) {
                            item.copy(checked = event.checked)
                        } else item
                    })
                }
                val targetItem = state.sections.flatMap { it.items }
                    .filterIsInstance<SettingItem.Toggle>()
                    .find { it.key == event.key }
                val isAutoSave = targetItem?.autoSave == true
                state.copy(
                    sections = updatedSections,
                    hasUnsavedChanges = if (isAutoSave) state.hasUnsavedChanges else true
                )
            }
            is SettingsUiEvent.ChoiceClicked -> state.copy(activeSelection = event.item)
            is SettingsUiEvent.DismissSelection -> state.copy(activeSelection = null)
            is SettingsUiEvent.ChoiceChanged -> {
                val updatedSections = state.sections.map { section ->
                    section.copy(items = section.items.map { item ->
                        if (item is SettingItem.Choice && item.key == event.key) {
                            item.copy(selectedOption = event.value)
                        } else item
                    })
                }
                val targetItem = state.sections.flatMap { it.items }
                    .filterIsInstance<SettingItem.Choice>()
                    .find { it.key == event.key }
                val isAutoSave = targetItem?.autoSave == true
                state.copy(
                    sections = updatedSections,
                    hasUnsavedChanges = if (isAutoSave) state.hasUnsavedChanges else true,
                    activeSelection = null
                )
            }
            SettingsUiEvent.SaveChanges -> state.copy(isSaving = true)
            SettingsUiEvent.DiscardChanges -> state.copy(
                sections = initialSections, 
                hasUnsavedChanges = false
            )
            is SettingsUiEvent.ResetToDefaults -> state.copy(
                // This would ideally come from a policy or defaults provider
                hasUnsavedChanges = true
            )
            else -> state
        }
    }
) {
    override fun onAction(action: SettingsUiEvent) {
        when (action) {
            SettingsUiEvent.NavigateBack -> sendEffect(SettingsUiEffect.NavigateBack)
            SettingsUiEvent.ResetToDefaults -> sendEffect(SettingsUiEffect.ShowResetDialog)
            SettingsUiEvent.ClearCache -> sendEffect(SettingsUiEffect.ShowClearCacheDialog)
            SettingsUiEvent.SaveChanges -> save()
            is SettingsUiEvent.ToggleChanged -> {
                currentState.sections.flatMap { it.items }
                    .filterIsInstance<SettingItem.Toggle>()
                    .find { it.key == action.key }
                    ?.onCheckedChange?.invoke(action.checked)
                dispatch(action)
            }
            is SettingsUiEvent.ChoiceChanged -> {
                currentState.sections.flatMap { it.items }
                    .filterIsInstance<SettingItem.Choice>()
                    .find { it.key == action.key }
                    ?.onOptionSelected?.invoke(action.value)
                dispatch(action)
            }
            else -> dispatch(action)
        }
    }

    private fun save() {
        if (currentState.isSaving) return
        dispatch(SettingsUiEvent.SaveChanges)
        launch {
            // Simulate save delay or actual persistence trigger
            // persist(currentState.sections)
            updateState { it.copy(isSaving = false, hasUnsavedChanges = false) }
            sendEffect(SettingsUiEffect.ShowToast("Settings saved"))
        }
    }

    fun updateSections(sections: List<SettingSection>) {
        updateState { it.copy(sections = sections, hasUnsavedChanges = false) }
    }
}
