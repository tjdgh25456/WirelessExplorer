package com.jsh.wirelessexplorer.core.data.ble

import com.jsh.wirelessexplorer.core.model.BleConnectedDevice
import com.jsh.wirelessexplorer.core.model.BleConnectionState
import com.jsh.wirelessexplorer.core.model.BleDevice
import com.jsh.wirelessexplorer.core.domain.ble.BleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BleRepositoryImpl @Inject constructor(
    private val scanner: BleScanner,
    private val connector: BleConnector,
) : BleRepository {

    override fun scanDevices(): Flow<List<BleDevice>> = scanner.scanDevices()

    override fun getScannedDevice(address: String): BleDevice? = scanner.getDevice(address)

    override fun connect(address: String): Flow<BleConnectionState> = connector.connect(address)

    override fun disconnect() = connector.disconnect()

    override suspend fun readCharacteristic(
        serviceUuid: String,
        charUuid: String,
    ): Result<ByteArray> = connector.readCharacteristic(serviceUuid, charUuid)

    override suspend fun writeCharacteristic(
        serviceUuid: String,
        charUuid: String,
        value: ByteArray,
    ): Result<Unit> = connector.writeCharacteristic(serviceUuid, charUuid, value)

    override fun observeCharacteristic(
        serviceUuid: String,
        charUuid: String,
    ): Flow<ByteArray> = connector.characteristicUpdates
        .filter { it.characteristicUuid == charUuid.lowercase() }
        .map { it.value }

    override fun isBluetoothEnabled(): Boolean = scanner.isBluetoothEnabled()

    override fun observeBluetoothEnabled(): Flow<Boolean> = scanner.observeBluetoothEnabled()

    override fun observeConnectedDevices(): Flow<List<BleConnectedDevice>> =
        connector.connectedDevicesFlow
}
