package com.jsh.wirelessexplorer.core.domain.ble

import javax.inject.Inject

class ReadGattCharacteristicUseCaseImpl @Inject constructor(
    private val bleRepository: BleRepository,
) : ReadGattCharacteristicUseCase {
    override suspend operator fun invoke(serviceUuid: String, charUuid: String): Result<ByteArray> =
        bleRepository.readCharacteristic(serviceUuid, charUuid)
}
