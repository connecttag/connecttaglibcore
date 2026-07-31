package org.connecttag.lib.kotlin.core.onboarding

object OnboardingCampaignDisplayPolicy {
    fun shouldShow(
        campaign: OnboardingCampaign,
        completions: List<OnboardingCompletion>,
    ): Boolean {
        if (campaign.showPolicy == OnboardingShowPolicy.ALWAYS) return true
        val matchingCampaign = completions.filter { completion ->
            completion.key.projectId == campaign.projectId &&
                completion.key.campaignId == campaign.campaignId
        }
        return when (campaign.showPolicy) {
            OnboardingShowPolicy.ALWAYS -> true
            OnboardingShowPolicy.FIRST_RUN -> matchingCampaign.isEmpty()
            OnboardingShowPolicy.ONCE_PER_VERSION -> matchingCampaign.none { completion ->
                completion.key.campaignVersion == campaign.version
            }
        }
    }
}
