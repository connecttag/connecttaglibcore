package org.connecttag.lib.kotlin.core.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.connecttag.lib.kotlin.core.utils.PageState

/**
 * A standard loading state view.
 */
@Composable
fun BaseLoadingView(
    modifier: Modifier = Modifier,
    message: String? = null
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        if (message != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = message, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

/**
 * A standard error state view with retry support.
 */
@Composable
fun BaseErrorView(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

/**
 * A standard empty state view.
 */
@Composable
fun BaseEmptyView(
    message: String,
    modifier: Modifier = Modifier,
    title: String? = null
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Inbox,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        if (title != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * A wrapper that handles [PageState] automatically.
 */
@Composable
fun <T> BaseStateWrapper(
    state: PageState<T>,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    loadingMessage: String? = null,
    emptyMessage: String? = null,
    content: @Composable (T) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                BaseLoadingView(message = loadingMessage)
            }
            state.error != null -> {
                BaseErrorView(
                    message = state.error.message ?: "An unexpected error occurred",
                    onRetry = onRetry
                )
            }
            state.data == null -> {
                BaseEmptyView(message = emptyMessage ?: "No data available")
            }
            else -> {
                content(state.data)
            }
        }
    }
}
