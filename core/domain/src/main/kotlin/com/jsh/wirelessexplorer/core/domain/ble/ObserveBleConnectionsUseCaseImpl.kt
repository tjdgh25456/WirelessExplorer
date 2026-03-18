package com.jsh.wirelessexplorer.core.domain.ble

import com.jsh.wirelessexplorer.core.model.BleConnectedDevice
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveBleConnectionsUseCaseImpl @Inject constructor(
    private val bleRepository: BleRepository,
) : ObserveBleConnectionsUseCase {
    override operator fun invoke(): Flow<List<BleConnectedDevice>> =
        bleRepository.observeConnectedDevices()
}
