package com.jsh.wirelessexplorer.core.domain.wifi

import com.jsh.wirelessexplorer.core.model.WifiConnectionInfo
import kotlinx.coroutines.flow.Flow

interface ObserveWifiConnectionUseCase {
    operator fun invoke(): Flow<WifiConnectionInfo?>
}
