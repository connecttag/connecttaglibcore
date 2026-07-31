package org.connecttag.lib.kotlin.core.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.connecttag.lib.kotlin.core.R

@Composable
fun OnboardingContent(
    state: OnboardingUiState,
    onSkip: () -> Unit,
    onFinish: () -> Unit,
    animationsEnabled: Boolean = true,
    pageImage: @Composable (
        page: OnboardingPage,
        contentDescription: String,
        modifier: Modifier,
    ) -> Unit = { page, desc, mod -> DefaultOnboardingPageImage(page, desc, mod) },
) {
    val pages = state.pages
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        if (state.isLoading || pages.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                CircularProgressIndicator()
                Text(
                    text = stringResource(R.string.onboarding_loading),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }
            return@Box
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (state.allowSkip) {
                TextButton(
                    onClick = onSkip,
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text(text = stringResource(R.string.onboarding_skip))
                }
            } else {
                Spacer(modifier = Modifier.height(48.dp))
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) { pageIndex ->
                OnboardingPageContent(
                    page = pages[pageIndex],
                    totalPages = pages.size,
                    pageImage = pageImage,
                )
            }

            PageIndicators(
                pageCount = pages.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier.padding(top = 16.dp, bottom = 20.dp),
            )

            val isLastPage = pagerState.currentPage == pages.lastIndex
            Button(
                onClick = {
                    if (isLastPage) {
                        onFinish()
                    } else {
                        coroutineScope.launch {
                            val nextPage = pagerState.currentPage + 1
                            if (animationsEnabled) {
                                pagerState.animateScrollToPage(nextPage)
                            } else {
                                pagerState.scrollToPage(nextPage)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp),
            ) {
                Text(
                    text = if (isLastPage) {
                        state.finishLabel?.takeIf(String::isNotBlank)
                            ?: stringResource(R.string.onboarding_start)
                    } else {
                        stringResource(R.string.onboarding_next)
                    },
                )
                if (!isLastPage) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    totalPages: Int,
    pageImage: @Composable (
        page: OnboardingPage,
        contentDescription: String,
        modifier: Modifier,
    ) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        pageImage(
            page,
            stringResource(R.string.onboarding_step_content_description, page.title),
            Modifier
                .fillMaxWidth(0.72f)
                .aspectRatio(1f),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(
                R.string.onboarding_step_progress,
                page.stepNumber,
                totalPages,
            ),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
internal fun DefaultOnboardingPageImage(
    page: OnboardingPage,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .semantics { this.contentDescription = contentDescription }
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = page.stepNumber.toString(),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PageIndicators(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val selected = index == currentPage
            Surface(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(width = if (selected) 22.dp else 8.dp, height = 8.dp)
                    .clip(CircleShape),
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                content = {},
            )
        }
    }
}
