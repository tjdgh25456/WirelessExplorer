package com.jsh.wirelessexplorer.core.domain.wifi

import kotlinx.coroutines.flow.Flow

interface ObserveWifiEnabledUseCase {
    operator fun invoke(): Flow<Boolean>
}
