package org.connecttag.lib.kotlin.core.theme

import android.content.Context
import org.connecttag.lib.kotlin.core.theme.engine.ThemeMotion
import org.connecttag.lib.kotlin.core.theme.branding.AppBrand
import org.connecttag.lib.kotlin.core.theme.branding.ConnectTagBrand
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

enum class ThemeMode { LIGHT, DARK, SYSTEM }

enum class AppLanguage(val value: String) {
    SYSTEM(""),
    ENGLISH("en"),
    ARABIC("ar");

    companion object {
        fun fromValue(value: String): AppLanguage = entries.find { it.value == value } ?: SYSTEM
    }
}

object ThemeSettings {
    private var settingsManager: SettingsManager? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    private var _customSeedColor by mutableStateOf<Color?>(null)
    var customSeedColor: Color?
        get() = _customSeedColor
        set(value) {
            if (_customSeedColor == value) return
            _customSeedColor = value
            scope.launch {
                settingsManager?.setSeedColor(value?.toArgb()?.toLong())
            }
        }

    private var _isDynamicColorEnabled by mutableStateOf(true)
    var isDynamicColorEnabled: Boolean
        get() = _isDynamicColorEnabled
        set(value) {
            if (_isDynamicColorEnabled == value) return
            _isDynamicColorEnabled = value
            scope.launch {
                settingsManager?.setDynamicColors(value)
            }
        }

    private var _themeMode by mutableStateOf(ThemeMode.SYSTEM)
    var themeMode: ThemeMode
        get() = _themeMode
        set(value) {
            if (_themeMode == value) return
            _themeMode = value
            scope.launch {
                settingsManager?.setThemeMode(value)
            }
        }

    private var _appLanguage by mutableStateOf(AppLanguage.SYSTEM)
    var appLanguage: AppLanguage
        get() = _appLanguage
        set(value) {
            if (_appLanguage == value) return
            _appLanguage = value
            scope.launch {
                settingsManager?.setLanguage(value)
            }
        }

    private val availableBrands = mutableMapOf<String, AppBrand>(
        ConnectTagBrand.id to ConnectTagBrand
    )

    private var _selectedBrand by mutableStateOf<AppBrand>(ConnectTagBrand)
    var selectedBrand: AppBrand
        get() = _selectedBrand
        set(value) {
            if (_selectedBrand == value) return
            _selectedBrand = value
            scope.launch {
                settingsManager?.setBrandId(value.id)
            }
        }

    fun registerBrand(brand: AppBrand) {
        availableBrands[brand.id] = brand
    }

    val expertColors = listOf(
        Color(0xFF6750A4), // Purple
        Color(0xFF0061A4), // Blue
        Color(0xFF006A60), // Teal
        Color(0xFF436916), // Green
        Color(0xFF8B5000), // Orange
        Color(0xFFB3261E), // Red
        Color(0xFF625B71), // Grey
        Color(0xFF7D5260), // Pink
        Color(0xFF006874), // Cyan
        Color(0xFF5D5F00)  // Lime
    )

    /**
     * Initializes the settings manager and loads saved values.
     * Call this in Application.onCreate or MainActivity.onCreate.
     */
    fun initialize(context: Context) {
        if (settingsManager != null) return
        
        val manager = SettingsManager(context.applicationContext)
        settingsManager = manager

        // Load initial values synchronously to avoid flicker on first frame
        runBlocking {
            val savedMode = manager.themeModeFlow.first()
            val savedColorLong = manager.seedColorFlow.first()
            val savedDynamic = manager.dynamicColorsFlow.first()
            val savedLanguage = manager.languageFlow.first()
            val savedBrandId = manager.brandIdFlow.first()

            _themeMode = savedMode
            _isDynamicColorEnabled = savedDynamic
            _appLanguage = savedLanguage
            _customSeedColor = savedColorLong?.let { Color(it.toInt()) }
            
            savedBrandId?.let { id ->
                availableBrands[id]?.let { _selectedBrand = it }
            }
        }
    }

    fun resetToDefaults() {
        scope.launch {
            settingsManager?.clearAllSettings()
            // Reset local states to default values immediately
            _themeMode = ThemeMode.SYSTEM
            _appLanguage = AppLanguage.SYSTEM
            _isDynamicColorEnabled = true
            _customSeedColor = null
            _selectedBrand = ConnectTagBrand
        }
    }
}
