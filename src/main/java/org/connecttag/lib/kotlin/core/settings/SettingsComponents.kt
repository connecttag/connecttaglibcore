package org.connecttag.lib.kotlin.core.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun SettingsDuotoneIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    tint: Color = MaterialTheme.colorScheme.primary,
    containerSize: Dp = 38.dp,
    iconSize: Dp = 20.dp,
) {
    SettingsDuotoneIconContainer(
        tint = tint,
        containerSize = containerSize,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize),
            tint = tint.takeOrElse { MaterialTheme.colorScheme.primary },
        )
    }
}

@Composable
fun SettingsDuotoneIcon(
    painter: Painter,
    contentDescription: String?,
    tint: Color = MaterialTheme.colorScheme.primary,
    containerSize: Dp = 38.dp,
    iconSize: Dp = 20.dp,
) {
    SettingsDuotoneIconContainer(
        tint = tint,
        containerSize = containerSize,
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize),
            tint = tint.takeOrElse { MaterialTheme.colorScheme.primary },
        )
    }
}

@Composable
private fun SettingsDuotoneIconContainer(
    tint: Color,
    containerSize: Dp,
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = Modifier.size(containerSize),
        shape = CircleShape,
        color = tint.copy(alpha = 0.12f),
    ) {
        Box(contentAlignment = Alignment.Center, content = content)
    }
}

@Composable
fun RenderSettingItem(
    item: SettingItem,
    onToggleChanged: ((String, Boolean) -> Unit)? = null,
    onChoiceClicked: ((SettingItem.Choice) -> Unit)? = null,
    onClick: ((String) -> Unit)? = null,
) {
    when (item) {
        is SettingItem.Clickable -> ClickableSettingRow(item, onClick)
        is SettingItem.Toggle -> ToggleSettingRow(item, onToggleChanged)
        is SettingItem.Choice -> ChoiceSettingRow(item, onChoiceClicked)
        is SettingItem.Action -> LegacyActionRow(item, onClick)
        is SettingItem.Custom -> item.content()
    }
}

@Composable
fun ClickableSettingRow(
    item: SettingItem.Clickable,
    onGlobalClick: ((String) -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = item.enabled) {
                onGlobalClick?.invoke(item.key) ?: item.onClick()
            },
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item.icon?.let {
                SettingsDuotoneIcon(
                    imageVector = it,
                    contentDescription = null,
                    tint = if (item.enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (item.enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
                item.summary?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            if (item.trailingContent != null) {
                item.trailingContent.invoke()
            } else {
                val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
                Icon(
                    imageVector = if (isRtl) Icons.AutoMirrored.Filled.KeyboardArrowLeft else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ToggleSettingRow(
    item: SettingItem.Toggle,
    onGlobalToggleChanged: ((String, Boolean) -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = item.enabled) {
                val newValue = !item.checked
                onGlobalToggleChanged?.invoke(item.key, newValue) ?: item.onCheckedChange(newValue)
            },
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item.icon?.let {
                SettingsDuotoneIcon(
                    imageVector = it,
                    contentDescription = null,
                    tint = if (item.enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (item.enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
                item.summary?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            if (item.trailingContent != null) {
                item.trailingContent.invoke()
            } else {
                Switch(
                    checked = item.checked,
                    onCheckedChange = {
                        onGlobalToggleChanged?.invoke(item.key, it) ?: item.onCheckedChange(it)
                    },
                    enabled = item.enabled
                )
            }
        }
    }
}

@Composable
fun ChoiceSettingRow(
    item: SettingItem.Choice,
    onGlobalChoiceClicked: ((SettingItem.Choice) -> Unit)? = null
) {
    val selectedOption = item.options.find { it.value == item.selectedOption }

    ClickableSettingRow(
        item = SettingItem.Clickable(
            key = item.key,
            title = item.title,
            summary = selectedOption?.title ?: item.summary,
            icon = item.icon,
            enabled = item.enabled,
            onClick = { onGlobalChoiceClicked?.invoke(item) }
        )
    )
}

@Composable
fun LegacyActionRow(
    item: SettingItem.Action,
    onGlobalClick: ((String) -> Unit)? = null
) {
    ClickableSettingRow(
        item = SettingItem.Clickable(
            key = item.key,
            title = item.title,
            summary = item.summary,
            icon = item.icon,
            enabled = item.enabled,
            onClick = { onGlobalClick?.invoke(item.key) ?: item.onClick() }
        )
    )
}

@Composable
fun RenderSettingSection(
    section: SettingSection,
    onToggleChanged: ((String, Boolean) -> Unit)? = null,
    onChoiceClicked: ((SettingItem.Choice) -> Unit)? = null,
    onClick: ((String) -> Unit)? = null,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            section.icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = section.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        
        section.description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
        
        Surface(
            modifier = Modifier.padding(horizontal = 8.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ) {
            Column {
                section.items.forEachIndexed { index, item ->
                    RenderSettingItem(
                        item = item,
                        onToggleChanged = onToggleChanged,
                        onChoiceClicked = onChoiceClicked,
                        onClick = onClick
                    )
                    if (index < section.items.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }
}
