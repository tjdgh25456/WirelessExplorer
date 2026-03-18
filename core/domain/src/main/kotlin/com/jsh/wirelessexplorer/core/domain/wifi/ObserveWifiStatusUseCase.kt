package com.jsh.wirelessexplorer.core.domain.wifi

import com.jsh.wirelessexplorer.core.model.WifiStatus
import kotlinx.coroutines.flow.Flow

interface ObserveWifiStatusUseCase {
    operator fun invoke(): Flow<WifiStatus?>
}
