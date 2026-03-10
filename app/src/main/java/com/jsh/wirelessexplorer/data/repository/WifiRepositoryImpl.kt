package com.jsh.wirelessexplorer.data.repository

import com.jsh.wirelessexplorer.data.datasource.WifiScanner
import com.jsh.wirelessexplorer.domain.model.WifiConnectionInfo
import com.jsh.wirelessexplorer.domain.model.WifiNetwork
import com.jsh.wirelessexplorer.domain.repository.WifiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WifiRepositoryImpl @Inject constructor(
    private val scanner: WifiScanner,
) : WifiRepository {

    override fun scanNetworks(): Flow<List<WifiNetwork>> = scanner.scanNetworks()

    override fun observeConnectionInfo(): Flow<WifiConnectionInfo?> = scanner.observeConnectionInfo()

    override fun isWifiEnabled(): Boolean = scanner.isWifiEnabled()
}
