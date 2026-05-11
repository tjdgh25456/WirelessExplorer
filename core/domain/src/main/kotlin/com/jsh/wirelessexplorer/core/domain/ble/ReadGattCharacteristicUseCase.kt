package com.jsh.wirelessexplorer.core.domain.ble

interface ReadGattCharacteristicUseCase {
    suspend operator fun invoke(serviceUuid: String, charUuid: String): Result<ByteArray>
}
