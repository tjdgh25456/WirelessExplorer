package com.jsh.wirelessexplorer.core.domain.uwb

import com.jsh.wirelessexplorer.core.model.UwbRangingResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StartUwbRangingUseCaseImpl @Inject constructor(
    private val repository: UwbRepository,
) : StartUwbRangingUseCase {
    override operator fun invoke(): Flow<UwbRangingResult> = repository.startRanging()
}
