package org.connecttag.lib.kotlin.core.theme.branding

import org.connecttag.lib.kotlin.core.theme.engine.ThemeMotion

enum class IconStyle {
    Filled,
    Outlined
}

interface AppBrand {
    val id: String
    val colors: BrandColors
    val motion: ThemeMotion
    val iconStyle: IconStyle
}
