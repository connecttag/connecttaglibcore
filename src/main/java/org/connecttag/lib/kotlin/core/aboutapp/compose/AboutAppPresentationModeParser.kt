package org.connecttag.lib.kotlin.core.aboutapp.compose

import org.connecttag.lib.kotlin.core.aboutapp.AboutAppPresentationMode

object AboutAppPresentationModeParser {
    fun parse(
        value: String?,
        mixedModeSelector: () -> AboutAppPresentationMode = { AboutAppPresentationMode.Dialog },
    ): AboutAppPresentationMode? {
        val normalized = value?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        return when {
            normalized.equals("dialog", ignoreCase = true) -> AboutAppPresentationMode.Dialog
            normalized.equals("bottomSheet", ignoreCase = true) ||
                normalized.equals("bottom_sheet", ignoreCase = true) ||
                normalized.equals("bottom-sheet", ignoreCase = true) -> AboutAppPresentationMode.BottomSheet
            normalized.equals("fullScreen", ignoreCase = true) ||
                normalized.equals("full_screen", ignoreCase = true) ||
                normalized.equals("full-screen", ignoreCase = true) ||
                normalized.equals("fullscreen", ignoreCase = true) -> AboutAppPresentationMode.FullScreen
            normalized.equals("mixed", ignoreCase = true) ||
                normalized.equals("random", ignoreCase = true) -> mixedModeSelector()
            else -> null
        }
    }
}
