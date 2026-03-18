package com.jsh.wirelessexplorer.core.domain.analyze

import app.cash.turbine.test
import com.jsh.wirelessexplorer.core.model.AnalyticsSummary
import com.jsh.wirelessexplorer.core.model.TechDistribution
import com.jsh.wirelessexplorer.core.model.TechType
import com.jsh.wirelessexplorer.core.model.TopDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ObserveTopDevicesUseCaseImplTest {

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val expected = listOf(
            TopDevice(deviceName = "Speaker", techType = TechType.BLE, connectCount = 10),
            TopDevice(deviceName = "Router", techType = TechType.WIFI, connectCount = 7),
        )
        val repository = FakeAnalyzeRepository(topDevices = expected)
        val useCase = ObserveTopDevicesUseCaseImpl(repository)

        useCase(limit = 2).test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}

private class FakeAnalyzeRepository(
    private val topDevices: List<TopDevice> = emptyList(),
) : AnalyzeRepository {
    override fun observeSummary(): Flow<AnalyticsSummary> = flowOf()
    override fun observeTechDistribution(): Flow<List<TechDistribution>> = flowOf()
    override fun observeTopDevices(limit: Int): Flow<List<TopDevice>> = flowOf(topDevices.take(limit))
}
