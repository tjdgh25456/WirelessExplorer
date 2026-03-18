package com.jsh.wirelessexplorer.core.domain.ble

import com.jsh.wirelessexplorer.core.model.BleConnectedDevice
import com.jsh.wirelessexplorer.core.model.BleConnectionState
import com.jsh.wirelessexplorer.core.model.BleDevice
import kotlinx.coroutines.flow.Flow

interface BleRepository {
    fun scanDevices(): Flow<List<BleDevice>>
    fun connect(address: String): Flow<BleConnectionState>
    fun disconnect()
    suspend fun readCharacteristic(serviceUuid: String, charUuid: String): Result<ByteArray>
    suspend fun writeCharacteristic(serviceUuid: String, charUuid: String, value: ByteArray): Result<Unit>
    fun observeCharacteristic(serviceUuid: String, charUuid: String): Flow<ByteArray>
    fun isBluetoothEnabled(): Boolean
    fun observeBluetoothEnabled(): Flow<Boolean>
    fun observeConnectedDevices(): Flow<List<BleConnectedDevice>>
}
