package com.jsh.wirelessexplorer.core.domain.wifi

import com.jsh.wirelessexplorer.core.model.WifiNetwork
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScanWifiNetworksUseCaseImpl @Inject constructor(
    private val repository: WifiRepository,
) : ScanWifiNetworksUseCase {
    override operator fun invoke(): Flow<List<WifiNetwork>> = repository.scanNetworks()
}
