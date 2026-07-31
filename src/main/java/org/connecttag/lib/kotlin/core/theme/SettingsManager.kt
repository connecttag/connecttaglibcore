package org.connecttag.lib.kotlin.core.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "connecttag_settings")

class SettingsManager(private val context: Context) {

    private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    private val SEED_COLOR_KEY = longPreferencesKey("seed_color")
    private val DYNAMIC_COLORS_KEY = booleanPreferencesKey("dynamic_colors")
    private val LANGUAGE_TAG_KEY = stringPreferencesKey("language_tag")
    private val BRAND_ID_KEY = stringPreferencesKey("brand_id")

    val themeModeFlow: Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        val mode = preferences[THEME_MODE_KEY] ?: ThemeMode.SYSTEM.name
        ThemeMode.valueOf(mode)
    }

    val seedColorFlow: Flow<Long?> = context.dataStore.data.map { preferences ->
        preferences[SEED_COLOR_KEY]
    }

    val dynamicColorsFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DYNAMIC_COLORS_KEY] ?: true
    }

    val languageFlow: Flow<AppLanguage> = context.dataStore.data.map { preferences ->
        val lang = preferences[LANGUAGE_TAG_KEY] ?: AppLanguage.SYSTEM.name
        AppLanguage.valueOf(lang)
    }

    val brandIdFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[BRAND_ID_KEY]
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode.name
        }
    }

    suspend fun setSeedColor(color: Long?) {
        context.dataStore.edit { preferences ->
            if (color != null) {
                preferences[SEED_COLOR_KEY] = color
            } else {
                preferences.remove(SEED_COLOR_KEY)
            }
        }
    }

    suspend fun setDynamicColors(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DYNAMIC_COLORS_KEY] = enabled
        }
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_TAG_KEY] = language.name
        }
    }

    suspend fun setBrandId(id: String) {
        context.dataStore.edit { preferences ->
            preferences[BRAND_ID_KEY] = id
        }
    }

    suspend fun clearAllSettings() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
