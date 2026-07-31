package org.connecttag.lib.kotlin.core.theme.branding

import androidx.compose.ui.graphics.Color
import org.connecttag.lib.kotlin.core.theme.engine.ThemeMotion

object ConnectTagBrand : AppBrand {
    override val id: String = "connecttag"
    override val colors: BrandColors = BrandColors.fromSeed(Color(0xFF6750A4))
    override val motion: ThemeMotion = ThemeMotion.Default
    override val iconStyle: IconStyle = IconStyle.Outlined
}
