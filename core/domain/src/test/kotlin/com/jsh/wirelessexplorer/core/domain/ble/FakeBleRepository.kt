package com.jsh.wirelessexplorer.core.domain.ble

import com.jsh.wirelessexplorer.core.model.BleConnectedDevice
import com.jsh.wirelessexplorer.core.model.BleConnectionState
import com.jsh.wirelessexplorer.core.model.BleDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class FakeBleRepository : BleRepository {

    var connectFlow: Flow<BleConnectionState> = flowOf()
    var scannedDevices: MutableMap<String, BleDevice> = mutableMapOf()

    override fun scanDevices(): Flow<List<BleDevice>> = emptyFlow()

    override fun getScannedDevice(address: String): BleDevice? = scannedDevices[address]

    override fun connect(address: String): Flow<BleConnectionState> = connectFlow

    override fun disconnect() = Unit

    override suspend fun readCharacteristic(serviceUuid: String, charUuid: String): Result<ByteArray> =
        Result.success(byteArrayOf())

    override suspend fun writeCharacteristic(serviceUuid: String, charUuid: String, value: ByteArray): Result<Unit> =
        Result.success(Unit)

    override fun observeCharacteristic(serviceUuid: String, charUuid: String): Flow<ByteArray> = emptyFlow()

    override fun isBluetoothEnabled(): Boolean = true

    override fun observeBluetoothEnabled(): Flow<Boolean> = flowOf(true)

    override fun observeConnectedDevices(): Flow<List<BleConnectedDevice>> = flowOf(emptyList())
}
