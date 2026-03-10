package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckOnboardingUseCase @Inject constructor(
    private val repository: OnboardingRepository,
) {
    operator fun invoke(): Flow<Boolean> = repository.isOnboardingCompleted()
}
