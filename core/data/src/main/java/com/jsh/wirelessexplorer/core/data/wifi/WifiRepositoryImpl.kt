package com.jsh.wirelessexplorer.core.data.wifi

import com.jsh.wirelessexplorer.core.model.WifiConnectionInfo
import com.jsh.wirelessexplorer.core.model.WifiNetwork
import com.jsh.wirelessexplorer.core.domain.wifi.WifiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WifiRepositoryImpl @Inject constructor(
    private val scanner: WifiScanner,
) : WifiRepository {

    override fun scanNetworks(): Flow<List<WifiNetwork>> = scanner.scanNetworks()

    override fun observeConnectionInfo(): Flow<WifiConnectionInfo?> = scanner.observeConnectionInfo()

    override fun isWifiEnabled(): Boolean = scanner.isWifiEnabled()

    override fun observeWifiEnabled(): Flow<Boolean> = scanner.observeWifiEnabled()
}
