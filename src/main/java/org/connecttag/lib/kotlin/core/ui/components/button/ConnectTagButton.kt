package org.connecttag.lib.kotlin.core.ui.components.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.connecttag.lib.kotlin.core.theme.engine.ConnectTagTheme

enum class ButtonVariant {
    Filled,
    Tonal,
    Outlined,
    Text,
}

/**
 * A highly customizable button with built-in loading state.
 */
@Composable
fun ConnectTagButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    variant: ButtonVariant = ButtonVariant.Filled,
    leadingIcon: (@Composable RowScope.() -> Unit)? = null,
    trailingIcon: (@Composable RowScope.() -> Unit)? = null,
) {
    val spacing = ConnectTagTheme.spacing
    val contentPadding = PaddingValues(
        horizontal = spacing.medium,
        vertical = spacing.small,
    )

    val content: @Composable RowScope.() -> Unit = {
        if (isLoading) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = when (variant) {
                        ButtonVariant.Filled -> MaterialTheme.colorScheme.onPrimary
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
            }
        } else {
            if (leadingIcon != null) leadingIcon()
            Text(text = text)
            if (trailingIcon != null) trailingIcon()
        }
    }

    // Disable clicks when loading
    val effectiveEnabled = enabled && !isLoading

    when (variant) {
        ButtonVariant.Filled -> Button(
            onClick = onClick,
            modifier = modifier,
            enabled = effectiveEnabled,
            contentPadding = contentPadding,
            content = content,
        )

        ButtonVariant.Tonal -> FilledTonalButton(
            onClick = onClick,
            modifier = modifier,
            enabled = effectiveEnabled,
            contentPadding = contentPadding,
            content = content,
        )

        ButtonVariant.Outlined -> OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = effectiveEnabled,
            contentPadding = contentPadding,
            content = content,
        )

        ButtonVariant.Text -> TextButton(
            onClick = onClick,
            modifier = modifier,
            enabled = effectiveEnabled,
            contentPadding = ButtonDefaults.TextButtonContentPadding,
            content = content,
        )
    }
}
