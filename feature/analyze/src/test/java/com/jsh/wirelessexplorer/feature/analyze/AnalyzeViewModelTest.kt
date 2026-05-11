package com.jsh.wirelessexplorer.feature.analyze

import app.cash.turbine.test
import com.jsh.wirelessexplorer.core.domain.analyze.ObserveAnalyticsSummaryUseCase
import com.jsh.wirelessexplorer.core.domain.analyze.ObserveTechDistributionUseCase
import com.jsh.wirelessexplorer.core.domain.analyze.ObserveTopDevicesUseCase
import com.jsh.wirelessexplorer.core.model.AnalyticsSummary
import com.jsh.wirelessexplorer.core.model.AnalyzePeriod
import com.jsh.wirelessexplorer.core.model.TechDistribution
import com.jsh.wirelessexplorer.core.model.TechType
import com.jsh.wirelessexplorer.core.model.TopDevice
import com.jsh.wirelessexplorer.core.testing.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AnalyzeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun uiState_combinesAllAnalyticsFlows() = runTest {
        val summary = AnalyticsSummary(deviceCount = 5, totalConnections = 12, bestSignal = -40)
        val distribution = listOf(
            TechDistribution(TechType.BLE, count = 3, percentage = 60f),
            TechDistribution(TechType.WIFI, count = 2, percentage = 40f),
        )
        val topDevices = listOf(
            TopDevice(deviceName = "Pixel Watch", techType = TechType.BLE, connectCount = 8),
        )

        val observeSummary = mockk<ObserveAnalyticsSummaryUseCase>()
        every { observeSummary.invoke() } returns flowOf(summary)

        val observeDistribution = mockk<ObserveTechDistributionUseCase>()
        every { observeDistribution.invoke() } returns flowOf(distribution)

        val observeTopDevices = mockk<ObserveTopDevicesUseCase>()
        every { observeTopDevices.invoke(any()) } returns flowOf(topDevices)

        val viewModel = AnalyzeViewModel(
            observeSummary = observeSummary,
            observeDistribution = observeDistribution,
            observeTopDevices = observeTopDevices,
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(summary, state.summary)
            assertEquals(2, state.techDistribution.size)
            assertEquals(1, state.topDevices.size)
            assertEquals("Pixel Watch", state.topDevices[0].deviceName)
            assertEquals(AnalyzePeriod.ALL_TIME, state.selectedPeriod)
            assertFalse(state.isLoading)
        }
    }
}
