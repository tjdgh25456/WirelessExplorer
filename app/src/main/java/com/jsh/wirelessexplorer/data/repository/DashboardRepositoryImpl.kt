package com.jsh.wirelessexplorer.data.repository

import com.jsh.wirelessexplorer.data.datasource.WifiStatusProvider
import com.jsh.wirelessexplorer.di.IoDispatcher
import com.jsh.wirelessexplorer.domain.model.DashboardStats
import com.jsh.wirelessexplorer.domain.model.WifiStatus
import com.jsh.wirelessexplorer.domain.repository.DashboardRepository
import com.jsh.wirelessexplorer.domain.repository.DeviceHistoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val wifiStatusProvider: WifiStatusProvider,
    private val deviceHistoryRepository: DeviceHistoryRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : DashboardRepository {

    override fun observeWifiStatus(): Flow<WifiStatus?> =
        wifiStatusProvider.observeWifiStatus()

    /** 디바이스 이력 통계 3개 Flow를 결합하여 DashboardStats로 변환한다. */
    override fun observeStats(): Flow<DashboardStats> = combine(
        deviceHistoryRepository.observeDeviceCount(),
        deviceHistoryRepository.observeTotalConnections(),
        deviceHistoryRepository.observeBestSignal(),
    ) { deviceCount, totalConnections, bestSignal ->
        DashboardStats(
            deviceCount = deviceCount,
            totalConnections = totalConnections ?: 0,
            bestSignal = bestSignal,
        )
    }.flowOn(ioDispatcher)
}
