package com.jsh.wirelessexplorer.core.domain.settings

import kotlinx.coroutines.flow.Flow

interface CheckOnboardingUseCase {
    operator fun invoke(): Flow<Boolean>
}
