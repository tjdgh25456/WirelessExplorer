package com.jsh.wirelessexplorer.core.domain.settings

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckOnboardingUseCaseImpl @Inject constructor(
    private val repository: OnboardingRepository,
) : CheckOnboardingUseCase {
    override operator fun invoke(): Flow<Boolean> = repository.isOnboardingCompleted()
}
