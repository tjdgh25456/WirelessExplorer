package com.jsh.wirelessexplorer.core.domain.ble

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveBluetoothEnabledUseCaseImpl @Inject constructor(
    private val bleRepository: BleRepository,
) : ObserveBluetoothEnabledUseCase {
    override operator fun invoke(): Flow<Boolean> = bleRepository.observeBluetoothEnabled()
}
