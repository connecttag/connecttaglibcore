package org.connecttag.lib.kotlin.core.aboutapp

data class AboutAppDialogLabels(
    val infoContentDescription: String,
    val closeContentDescription: String,
    val websiteTitle: String,
)

data class AboutAppWebsite(
    val label: String,
    val url: String,
)

data class AboutAppRepository(
    val label: String,
    val url: String,
)

enum class AboutAppPresentationMode {
    Dialog,
    BottomSheet,
    FullScreen,
}

data class AboutAppPresentationOptions(
    val mode: AboutAppPresentationMode = AboutAppPresentationMode.Dialog,
    val showCloseButton: Boolean = true,
    val dismissOnBack: Boolean = true,
    val maxDialogWidthDp: Int = 420,
    val showDragHandle: Boolean = true,
    val socialSpacingDp: Int = 8,
)
