package com.jsh.wirelessexplorer.data.repository

import com.jsh.wirelessexplorer.data.datasource.BleConnector
import com.jsh.wirelessexplorer.data.datasource.BleScanner
import com.jsh.wirelessexplorer.domain.model.BleConnectionState
import com.jsh.wirelessexplorer.domain.model.BleDevice
import com.jsh.wirelessexplorer.domain.repository.BleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BleRepositoryImpl @Inject constructor(
    private val scanner: BleScanner,
    private val connector: BleConnector,
) : BleRepository {

    override fun scanDevices(): Flow<List<BleDevice>> = scanner.scanDevices()

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
}
