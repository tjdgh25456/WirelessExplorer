package com.jsh.wirelessexplorer.core.domain.settings

import javax.inject.Inject

class CompleteOnboardingUseCaseImpl @Inject constructor(
    private val repository: OnboardingRepository,
) : CompleteOnboardingUseCase {
    override suspend operator fun invoke() {
        repository.setOnboardingCompleted()
    }
}
