package org.connecttag.lib.kotlin.core.ui.components.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AkScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable (() -> Unit)? = null,
    bottomBar: @Composable (() -> Unit)? = null,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarHost: (@Composable () -> Unit)? = null,
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    floatingActionButton: @Composable (() -> Unit)? = null,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar ?: {},
        bottomBar = bottomBar ?: {},
        snackbarHost = snackbarHost ?: {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        floatingActionButton = floatingActionButton ?: {},
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        content = content
    )
}
