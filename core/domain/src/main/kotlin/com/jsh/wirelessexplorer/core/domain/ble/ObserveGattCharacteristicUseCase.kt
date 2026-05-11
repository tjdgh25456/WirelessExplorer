package com.jsh.wirelessexplorer.core.domain.ble

import kotlinx.coroutines.flow.Flow

interface ObserveGattCharacteristicUseCase {
    operator fun invoke(serviceUuid: String, charUuid: String): Flow<ByteArray>
}
