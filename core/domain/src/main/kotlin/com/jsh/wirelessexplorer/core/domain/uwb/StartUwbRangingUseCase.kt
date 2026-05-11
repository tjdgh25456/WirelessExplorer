package com.jsh.wirelessexplorer.core.domain.uwb

import com.jsh.wirelessexplorer.core.model.UwbRangingResult
import kotlinx.coroutines.flow.Flow

interface StartUwbRangingUseCase {
    operator fun invoke(): Flow<UwbRangingResult>
}
