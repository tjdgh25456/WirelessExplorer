package com.jsh.wirelessexplorer.core.domain.ble

import com.jsh.wirelessexplorer.core.common.TimeProvider
import com.jsh.wirelessexplorer.core.model.BleConnectionState
import com.jsh.wirelessexplorer.core.model.DeviceHistory
import com.jsh.wirelessexplorer.core.model.TechType
import com.jsh.wirelessexplorer.core.domain.history.DeviceHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ConnectBleDeviceUseCaseImpl @Inject constructor(
    private val bleRepository: BleRepository,
    private val historyRepository: DeviceHistoryRepository,
    private val timeProvider: TimeProvider,
) : ConnectBleDeviceUseCase {

    override operator fun invoke(address: String, deviceName: String): Flow<BleConnectionState> =
        bleRepository.connect(address).onEach { state ->
            if (state is BleConnectionState.Connected) {
                historyRepository.upsert(
                    DeviceHistory(
                        deviceName = deviceName,
                        deviceAddress = address,
                        techType = TechType.BLE,
                        lastConnectedAt = timeProvider.now(),
                    ),
                )
                historyRepository.incrementConnectCount(address, TechType.BLE)
            }
        }
}
