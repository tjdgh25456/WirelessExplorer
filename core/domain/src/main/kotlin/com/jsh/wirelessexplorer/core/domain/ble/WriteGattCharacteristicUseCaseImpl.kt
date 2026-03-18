package com.jsh.wirelessexplorer.core.domain.ble

import javax.inject.Inject

class WriteGattCharacteristicUseCaseImpl @Inject constructor(
    private val bleRepository: BleRepository,
) : WriteGattCharacteristicUseCase {
    override suspend operator fun invoke(
        serviceUuid: String,
        charUuid: String,
        value: ByteArray,
    ): Result<Unit> = bleRepository.writeCharacteristic(serviceUuid, charUuid, value)
}
