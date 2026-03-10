package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.repository.BleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveGattCharacteristicUseCase @Inject constructor(
    private val bleRepository: BleRepository,
) {
    operator fun invoke(serviceUuid: String, charUuid: String): Flow<ByteArray> =
        bleRepository.observeCharacteristic(serviceUuid, charUuid)
}
