package org.connecttag.lib.kotlin.core.permissions

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.connecttag.lib.kotlin.core.R

@Composable
fun PermissionRationaleDialog(
    title: String? = null,
    description: String,
    confirmText: String? = null,
    dismissText: String? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    icon: @Composable (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = icon,
        title = {
            Text(
                text = title ?: stringResource(R.string.permission_rationale_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(confirmText ?: stringResource(R.string.permission_rationale_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText ?: stringResource(R.string.permission_rationale_dismiss))
            }
        }
    )
}

@Composable
fun PermanentlyDeniedDialog(
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.permission_settings_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = stringResource(R.string.permission_settings_description),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(onClick = onOpenSettings) {
                Text(stringResource(R.string.permission_settings_open))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
