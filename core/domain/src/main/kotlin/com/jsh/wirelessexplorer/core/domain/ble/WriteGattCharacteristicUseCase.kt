package com.jsh.wirelessexplorer.core.domain.ble

interface WriteGattCharacteristicUseCase {
    suspend operator fun invoke(serviceUuid: String, charUuid: String, value: ByteArray): Result<Unit>
}
