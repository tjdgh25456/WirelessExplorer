package com.jsh.wirelessexplorer.domain.repository

import com.jsh.wirelessexplorer.domain.model.WifiConnectionInfo
import com.jsh.wirelessexplorer.domain.model.WifiNetwork
import kotlinx.coroutines.flow.Flow

interface WifiRepository {

    fun scanNetworks(): Flow<List<WifiNetwork>>

    fun observeConnectionInfo(): Flow<WifiConnectionInfo?>

    fun isWifiEnabled(): Boolean
}
