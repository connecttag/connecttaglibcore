package org.connecttag.lib.kotlin.core.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
fun RenderSettingItem(item: SettingItem) {
    when (item) {
        is SettingItem.Clickable -> ClickableSettingRow(item)
        is SettingItem.Toggle -> ToggleSettingRow(item)
        is SettingItem.Choice -> ChoiceSettingRow(item)
        is SettingItem.Action -> LegacyActionRow(item)
        is SettingItem.Custom -> item.content()
    }
}

@Composable
fun ClickableSettingRow(item: SettingItem.Clickable) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = item.enabled, onClick = item.onClick),
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
fun ToggleSettingRow(item: SettingItem.Toggle) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = item.enabled) { item.onCheckedChange(!item.checked) },
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
                    onCheckedChange = item.onCheckedChange,
                    enabled = item.enabled
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceSettingRow(item: SettingItem.Choice) {
    val selectedOption = item.options.find { it.value == item.selectedOption }
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ClickableSettingRow(
        item = SettingItem.Clickable(
            key = item.key,
            title = item.title,
            summary = selectedOption?.title ?: item.summary,
            icon = item.icon,
            enabled = item.enabled,
            onClick = { showSheet = true }
        )
    )

    if (showSheet) {
        SelectionBottomSheet(
            sheetState = sheetState,
            title = item.title,
            icon = item.icon,
            options = item.options,
            selectedValue = item.selectedOption,
            onValueChange = { newValue ->
                scope.launch {
                    item.onOptionSelected(newValue)
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showSheet = false
                    }
                }
            },
            onDismiss = { showSheet = false }
        )
    }
}

@Composable
fun LegacyActionRow(item: SettingItem.Action) {
    ClickableSettingRow(
        item = SettingItem.Clickable(
            key = item.key,
            title = item.title,
            summary = item.summary,
            icon = item.icon,
            enabled = item.enabled,
            onClick = item.onClick
        )
    )
}

@Composable
fun RenderSettingSection(section: SettingSection) {
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
                    RenderSettingItem(item)
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

@Composable
fun OverviewValueRow(
    label: String,
    value: String,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = color.copy(alpha = 0.7f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = color,
            textAlign = TextAlign.End,
        )
    }
}
