package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.repository.BleRepository
import javax.inject.Inject

class ReadGattCharacteristicUseCase @Inject constructor(
    private val bleRepository: BleRepository,
) {
    suspend operator fun invoke(serviceUuid: String, charUuid: String): Result<ByteArray> =
        bleRepository.readCharacteristic(serviceUuid, charUuid)
}
