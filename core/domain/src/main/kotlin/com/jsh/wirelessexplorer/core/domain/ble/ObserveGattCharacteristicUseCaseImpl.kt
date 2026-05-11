package com.jsh.wirelessexplorer.core.domain.ble

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveGattCharacteristicUseCaseImpl @Inject constructor(
    private val bleRepository: BleRepository,
) : ObserveGattCharacteristicUseCase {
    override operator fun invoke(serviceUuid: String, charUuid: String): Flow<ByteArray> =
        bleRepository.observeCharacteristic(serviceUuid, charUuid)
}
