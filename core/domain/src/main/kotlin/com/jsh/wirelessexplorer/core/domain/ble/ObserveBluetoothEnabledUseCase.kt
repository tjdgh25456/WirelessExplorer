package com.jsh.wirelessexplorer.core.domain.ble

import kotlinx.coroutines.flow.Flow

interface ObserveBluetoothEnabledUseCase {
    operator fun invoke(): Flow<Boolean>
}
