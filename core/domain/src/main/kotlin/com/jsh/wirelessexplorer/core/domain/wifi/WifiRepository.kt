package com.jsh.wirelessexplorer.core.domain.wifi

import com.jsh.wirelessexplorer.core.model.WifiConnectionInfo
import com.jsh.wirelessexplorer.core.model.WifiNetwork
import kotlinx.coroutines.flow.Flow

interface WifiRepository {

    fun scanNetworks(): Flow<List<WifiNetwork>>

    fun observeConnectionInfo(): Flow<WifiConnectionInfo?>

    fun isWifiEnabled(): Boolean

    fun observeWifiEnabled(): Flow<Boolean>
}
