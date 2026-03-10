package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.WifiNetwork
import com.jsh.wirelessexplorer.domain.repository.WifiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScanWifiNetworksUseCase @Inject constructor(
    private val repository: WifiRepository,
) {
    operator fun invoke(): Flow<List<WifiNetwork>> = repository.scanNetworks()
}
