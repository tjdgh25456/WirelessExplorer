package com.jsh.wirelessexplorer.data.repository

import com.jsh.wirelessexplorer.data.datasource.WifiScanner
import com.jsh.wirelessexplorer.di.IoDispatcher
import com.jsh.wirelessexplorer.domain.model.WifiConnectionInfo
import com.jsh.wirelessexplorer.domain.model.WifiNetwork
import com.jsh.wirelessexplorer.domain.repository.WifiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WifiRepositoryImpl @Inject constructor(
    private val scanner: WifiScanner,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : WifiRepository {

    override fun scanNetworks(): Flow<List<WifiNetwork>> = scanner.scanNetworks()

    override fun observeConnectionInfo(): Flow<WifiConnectionInfo?> = flow {
        emit(scanner.getConnectionInfo())
    }.flowOn(ioDispatcher)

    override fun isWifiEnabled(): Boolean = scanner.isWifiEnabled()
}
