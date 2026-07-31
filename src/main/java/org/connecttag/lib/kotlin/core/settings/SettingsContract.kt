package org.connecttag.lib.kotlin.core.settings

import androidx.compose.ui.graphics.vector.ImageVector
import org.connecttag.lib.kotlin.core.mvi.MviAction
import org.connecttag.lib.kotlin.core.mvi.MviEffect
import org.connecttag.lib.kotlin.core.mvi.MviState

data class SettingsUiState(
    val sections: List<SettingSection> = emptyList(),
    val hasUnsavedChanges: Boolean = false,
    val isSaving: Boolean = false,
    val activeSelection: SettingItem.Choice? = null,
) : MviState

sealed interface SettingsUiEvent : MviAction {
    data object NavigateBack : SettingsUiEvent
    data object SaveChanges : SettingsUiEvent
    data object DiscardChanges : SettingsUiEvent
    data object ResetToDefaults : SettingsUiEvent
    data class ToggleChanged(val key: String, val checked: Boolean) : SettingsUiEvent
    data class ChoiceClicked(val item: SettingItem.Choice) : SettingsUiEvent
    data class ChoiceChanged(val key: String, val value: String) : SettingsUiEvent
    data object DismissSelection : SettingsUiEvent
    data object ClearCache : SettingsUiEvent
}

sealed interface SettingsUiEffect : MviEffect {
    data object NavigateBack : SettingsUiEffect
    data class ShowToast(val message: String) : SettingsUiEffect
    data object ShowResetDialog : SettingsUiEffect
    data object ShowClearCacheDialog : SettingsUiEffect
}
