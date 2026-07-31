package org.connecttag.lib.kotlin.core.privacypolicy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.connecttag.lib.kotlin.core.R
import org.connecttag.lib.kotlin.core.ui.components.scaffold.AkScaffold
import org.connecttag.lib.kotlin.core.actions.PlatformLinkOpener
import org.connecttag.lib.kotlin.core.actions.PlatformLinkRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    state: PrivacyPolicyUiState,
    onBack: () -> Unit,
    linkOpener: PlatformLinkOpener,
    onRetry: () -> Unit,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    allowDismiss: Boolean,
    modifier: Modifier = Modifier,
) {
    val displayTitle = if (state.documents.count(LegalPolicyDisplayDocument::hasDisplayContent) > 1) {
        stringResource(R.string.privacy_policy_title)
    } else if (state.title.isBlank()) {
        stringResource(R.string.privacy_policy_title)
    } else {
        state.title
    }
    AkScaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = displayTitle) },
                navigationIcon = {
                    if (allowDismiss) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.privacy_policy_back),
                            )
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        PrivacyPolicyContent(
            state = state,
            linkOpener = linkOpener,
            onRetry = onRetry,
            onAccept = onAccept,
            onDecline = onDecline,
            allowDecline = allowDismiss,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        )
    }
}

@Composable
fun PrivacyPolicyContent(
    state: PrivacyPolicyUiState,
    linkOpener: PlatformLinkOpener,
    onRetry: () -> Unit,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    allowDecline: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        val contentModifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .verticalScroll(rememberScrollState())

        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = stringResource(R.string.privacy_policy_loading),
                        modifier = Modifier.padding(top = 16.dp),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            !state.enabled -> {
                Text(
                    text = stringResource(R.string.privacy_policy_unavailable),
                    modifier = contentModifier,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }

            state.documents.any(LegalPolicyDisplayDocument::hasDisplayContent) -> {
                Column(
                    modifier = contentModifier,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    state.documents
                        .filter(LegalPolicyDisplayDocument::hasDisplayContent)
                        .forEachIndexed { index, document ->
                            if (index > 0) HorizontalDivider()
                            LegalPolicyDocumentContent(
                                document = document,
                                linkOpener = linkOpener,
                            )
                        }
                }
            }

            state.type == PrivacyPolicyType.URL && !state.url.isNullOrBlank() -> {
                val url = state.url.orEmpty()
                Column(
                    modifier = contentModifier,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = stringResource(R.string.privacy_policy_open_browser),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Button(
                        onClick = {
                            linkOpener.open(PlatformLinkRequest(url = url, title = state.title))
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = stringResource(R.string.privacy_policy_open_url))
                    }
                }
            }

            state.content.isNotBlank() -> {
                Column(
                    modifier = contentModifier,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    val version = state.documentKey?.version
                    val effectiveDate = state.effectiveAt?.substringBefore('T')
                    if (!version.isNullOrBlank() && !effectiveDate.isNullOrBlank()) {
                        Text(
                            text = stringResource(
                                R.string.privacy_policy_version_effective,
                                version,
                                effectiveDate,
                            ),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Text(
                        text = state.content,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            else -> {
                Column(
                    modifier = contentModifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(R.string.privacy_policy_unavailable),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                    )
                    OutlinedButton(
                        onClick = onRetry,
                        modifier = Modifier.padding(top = 16.dp),
                    ) {
                        Text(text = stringResource(R.string.privacy_policy_retry))
                    }
                }
            }
        }

        if (state.enabled && state.hasDisplayContent) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (state.isMandatory && allowDecline) {
                    OutlinedButton(
                        onClick = onDecline,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = stringResource(R.string.privacy_policy_decline))
                    }
                }
                Button(
                    onClick = onAccept,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = stringResource(
                            if (state.isMandatory) R.string.privacy_policy_accept else R.string.privacy_policy_close,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun LegalPolicyDocumentContent(
    document: LegalPolicyDisplayDocument,
    linkOpener: PlatformLinkOpener,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = document.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        val version = document.documentKey?.version
        val effectiveDate = document.effectiveAt?.substringBefore('T')
        if (!version.isNullOrBlank() && !effectiveDate.isNullOrBlank()) {
            Text(
                text = stringResource(
                    R.string.privacy_policy_version_effective,
                    version,
                    effectiveDate,
                ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        when (document.type) {
            PrivacyPolicyType.CONTENT -> Text(
                text = document.content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )

            PrivacyPolicyType.URL -> {
                val url = document.url.orEmpty()
                Button(
                    onClick = {
                        linkOpener.open(
                            PlatformLinkRequest(url = url, title = document.title),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = stringResource(R.string.privacy_policy_open_url))
                }
            }

            PrivacyPolicyType.NONE -> Unit
        }
    }
}
