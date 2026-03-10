package com.jsh.wirelessexplorer.data.repository

import com.jsh.wirelessexplorer.domain.model.AnalyticsSummary
import com.jsh.wirelessexplorer.domain.model.TechDistribution
import com.jsh.wirelessexplorer.domain.model.TopDevice
import com.jsh.wirelessexplorer.domain.repository.AnalyzeRepository
import com.jsh.wirelessexplorer.domain.repository.DeviceHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnalyzeRepositoryImpl @Inject constructor(
    private val historyRepository: DeviceHistoryRepository,
) : AnalyzeRepository {

    override fun observeSummary(): Flow<AnalyticsSummary> = combine(
        historyRepository.observeDeviceCount(),
        historyRepository.observeTotalConnections(),
        historyRepository.observeBestSignal(),
    ) { deviceCount, totalConnections, bestSignal ->
        AnalyticsSummary(
            deviceCount = deviceCount,
            totalConnections = totalConnections ?: 0,
            bestSignal = bestSignal,
        )
    }

    /** 기술별 디바이스 수와 비율을 계산한다. */
    override fun observeTechDistribution(): Flow<List<TechDistribution>> =
        historyRepository.observeAll().map { devices ->
            val total = devices.size.coerceAtLeast(1)
            devices.groupBy { it.techType }
                .map { (techType, group) ->
                    TechDistribution(
                        techType = techType,
                        count = group.size,
                        percentage = group.size.toFloat() / total,
                    )
                }
                .sortedByDescending { it.count }
        }

    /** 접속 횟수 기준 상위 디바이스를 반환한다. */
    override fun observeTopDevices(limit: Int): Flow<List<TopDevice>> =
        historyRepository.observeAll().map { devices ->
            devices.sortedByDescending { it.connectCount }
                .take(limit)
                .map { device ->
                    TopDevice(
                        deviceName = device.deviceName,
                        techType = device.techType,
                        connectCount = device.connectCount,
                    )
                }
        }
}
