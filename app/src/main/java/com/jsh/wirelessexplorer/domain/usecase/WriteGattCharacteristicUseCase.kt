package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.repository.BleRepository
import javax.inject.Inject

class WriteGattCharacteristicUseCase @Inject constructor(
    private val bleRepository: BleRepository,
) {
    suspend operator fun invoke(
        serviceUuid: String,
        charUuid: String,
        value: ByteArray,
    ): Result<Unit> = bleRepository.writeCharacteristic(serviceUuid, charUuid, value)
}
