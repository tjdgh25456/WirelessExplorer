package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.UwbRangingResult
import com.jsh.wirelessexplorer.domain.repository.UwbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StartUwbRangingUseCase @Inject constructor(
    private val repository: UwbRepository,
) {
    operator fun invoke(): Flow<UwbRangingResult> = repository.startRanging()
}
