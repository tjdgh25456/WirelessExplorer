package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.repository.OnboardingRepository
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val repository: OnboardingRepository,
) {
    suspend operator fun invoke() {
        repository.setOnboardingCompleted()
    }
}
