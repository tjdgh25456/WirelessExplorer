package com.jsh.wirelessexplorer.core.domain.wifi

import com.jsh.wirelessexplorer.core.model.WifiNetwork
import kotlinx.coroutines.flow.Flow

interface ScanWifiNetworksUseCase {
    operator fun invoke(): Flow<List<WifiNetwork>>
}
