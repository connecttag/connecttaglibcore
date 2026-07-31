package org.connecttag.lib.kotlin.core.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.connecttag.lib.kotlin.core.R
import org.connecttag.lib.kotlin.core.actions.ActionRequestHandler
import org.connecttag.lib.kotlin.core.actions.ActionRequestFactory
import org.connecttag.lib.kotlin.core.ui.components.scaffold.AkScaffold

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NotificationDetailScreen(
    uiState: NotificationDetailUiState,
    onBack: () -> Unit,
    onAction: ActionRequestHandler,
) {
    AkScaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.notifications_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.notifications_navigation_back_content_description)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val notification = uiState.notification
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                notification == null -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.notifications_detail_missing_title),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = uiState.error ?: stringResource(R.string.notifications_detail_missing_message),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Text(
                            text = notification.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        notification.timestamp?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = notification.message,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        if (notification.actions.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                notification.actions.forEach { action ->
                                    Button(
                                        onClick = {
                                            ActionRequestFactory.from(action.type, action.content?.toString())?.let {
                                                onAction(it)
                                            }
                                        }
                                    ) {
                                        Text(action.label ?: stringResource(R.string.notifications_open_action))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
