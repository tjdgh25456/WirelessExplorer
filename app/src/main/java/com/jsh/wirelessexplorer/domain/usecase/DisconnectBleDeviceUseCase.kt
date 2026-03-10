package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.repository.BleRepository
import javax.inject.Inject

class DisconnectBleDeviceUseCase @Inject constructor(
    private val bleRepository: BleRepository,
) {
    operator fun invoke() = bleRepository.disconnect()
}
