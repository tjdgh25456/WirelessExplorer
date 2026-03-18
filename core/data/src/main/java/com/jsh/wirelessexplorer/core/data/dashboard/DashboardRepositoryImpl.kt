package com.jsh.wirelessexplorer.core.data.dashboard

import com.jsh.wirelessexplorer.core.common.IoDispatcher
import com.jsh.wirelessexplorer.core.model.DashboardStats
import com.jsh.wirelessexplorer.core.model.TrafficSpeed
import com.jsh.wirelessexplorer.core.model.WifiStatus
import com.jsh.wirelessexplorer.core.domain.dashboard.DashboardRepository
import com.jsh.wirelessexplorer.core.domain.history.DeviceHistoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val wifiStatusProvider: WifiStatusProvider,
    private val trafficStatsProvider: TrafficStatsProvider,
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

    override fun observeTrafficSpeed(): Flow<TrafficSpeed> =
        trafficStatsProvider.observeTrafficSpeed()
}
