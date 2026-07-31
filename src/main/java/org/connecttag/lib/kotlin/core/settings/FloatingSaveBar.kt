package org.connecttag.lib.kotlin.core.settings

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.connecttag.lib.kotlin.core.R

@Composable
fun FloatingSaveBar(
    isVisible: Boolean,
    isSaving: Boolean,
    onSave: () -> Unit,
    onDiscard: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
            tonalElevation = 4.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.settings_unsaved_changes),
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onDiscard, enabled = !isSaving) {
                        Text(stringResource(R.string.discard_changes))
                    }
                    Button(
                        onClick = onSave,
                        enabled = !isSaving,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(stringResource(R.string.save_all_changes))
                        }
                    }
                }
            }
        }
    }
}
