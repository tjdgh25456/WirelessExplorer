package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.BleConnectionState
import com.jsh.wirelessexplorer.domain.model.DeviceHistory
import com.jsh.wirelessexplorer.domain.model.TechType
import com.jsh.wirelessexplorer.domain.repository.BleRepository
import com.jsh.wirelessexplorer.domain.repository.DeviceHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ConnectBleDeviceUseCase @Inject constructor(
    private val bleRepository: BleRepository,
    private val historyRepository: DeviceHistoryRepository,
) {

    /**
     * BLE 디바이스에 연결하고, 연결 성공 시 히스토리에 저장한다.
     */
    operator fun invoke(address: String, deviceName: String): Flow<BleConnectionState> =
        bleRepository.connect(address).onEach { state ->
            if (state is BleConnectionState.Connected) {
                // 연결 성공 시 디바이스 히스토리에 기록
                historyRepository.upsert(
                    DeviceHistory(
                        deviceName = deviceName,
                        deviceAddress = address,
                        techType = TechType.BLE,
                    ),
                )
                historyRepository.incrementConnectCount(address, TechType.BLE)
            }
        }
}
