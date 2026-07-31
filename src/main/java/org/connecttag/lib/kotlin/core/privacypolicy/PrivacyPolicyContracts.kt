package org.connecttag.lib.kotlin.core.privacypolicy

import org.connecttag.lib.kotlin.core.mvi.MviAction
import org.connecttag.lib.kotlin.core.mvi.MviEffect
import org.connecttag.lib.kotlin.core.mvi.MviState
import kotlinx.serialization.Serializable

@Serializable
data class PrivacyPolicyConfig(
    val enabled: Boolean,
    val resourceKey: String,
    val requiresConsent: Boolean,
)

@Serializable
data class ConsentDocumentKey(
    val projectId: String,
    val documentId: String,
    val documentType: String,
    val version: String,
    val contentHash: String,
)

enum class PrivacyPolicyType {
    URL,
    CONTENT,
    NONE,
}

data class PrivacyPolicyDisplayContent(
    val title: String,
    val type: PrivacyPolicyType,
    val url: String? = null,
    val content: String = "",
    val documentKey: ConsentDocumentKey? = null,
    val isMandatory: Boolean,
    val effectiveAt: String? = null,
    val documents: List<LegalPolicyDisplayDocument> = emptyList(),
)

data class LegalPolicyDisplayDocument(
    val title: String,
    val documentType: String,
    val type: PrivacyPolicyType,
    val url: String? = null,
    val content: String = "",
    val documentKey: ConsentDocumentKey? = null,
    val isMandatory: Boolean = false,
    val effectiveAt: String? = null,
) {
    val hasDisplayContent: Boolean
        get() = when (type) {
            PrivacyPolicyType.URL -> !url.isNullOrBlank()
            PrivacyPolicyType.CONTENT -> content.isNotBlank()
            PrivacyPolicyType.NONE -> false
        }
}

data class PrivacyPolicyUiState(
    val isLoading: Boolean = true,
    val enabled: Boolean = true,
    val title: String = "",
    val type: PrivacyPolicyType = PrivacyPolicyType.NONE,
    val url: String? = null,
    val content: String = "",
    val documentKey: ConsentDocumentKey? = null,
    val isMandatory: Boolean = false,
    val effectiveAt: String? = null,
    val documents: List<LegalPolicyDisplayDocument> = emptyList(),
) : MviState {
    val hasDisplayContent: Boolean
        get() = documents.any(LegalPolicyDisplayDocument::hasDisplayContent) || when (type) {
            PrivacyPolicyType.URL -> !url.isNullOrBlank()
            PrivacyPolicyType.CONTENT -> content.isNotBlank()
            PrivacyPolicyType.NONE -> false
        }

    val requiredDocumentKeys: List<ConsentDocumentKey>
        get() = documents
            .asSequence()
            .filter(LegalPolicyDisplayDocument::isMandatory)
            .mapNotNull(LegalPolicyDisplayDocument::documentKey)
            .distinct()
            .toList()
            .ifEmpty { listOfNotNull(documentKey.takeIf { isMandatory }) }
}

sealed interface PrivacyPolicyUiEvent : MviAction {
    data class Load(val config: PrivacyPolicyConfig) : PrivacyPolicyUiEvent
    data object Retry : PrivacyPolicyUiEvent
    data object Accept : PrivacyPolicyUiEvent
    data object Decline : PrivacyPolicyUiEvent
    data object NavigateBack : PrivacyPolicyUiEvent
}

sealed interface PrivacyPolicyUiEffect : MviEffect {
    data object Accepted : PrivacyPolicyUiEffect
    data object NavigateBack : PrivacyPolicyUiEffect
}

interface PrivacyPolicyContentProvider {
    suspend fun loadContent(
        projectKey: String,
        resourceKey: String,
    ): Result<PrivacyPolicyDisplayContent>
}

interface PrivacyConsentStore {
    suspend fun acceptConsent(documentKey: ConsentDocumentKey)
}
