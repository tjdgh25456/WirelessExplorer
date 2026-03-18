package com.jsh.wirelessexplorer.feature.wifi

import app.cash.turbine.test
import com.jsh.wirelessexplorer.core.domain.dashboard.ObserveTrafficSpeedUseCase
import com.jsh.wirelessexplorer.core.domain.wifi.ObserveWifiConnectionUseCase
import com.jsh.wirelessexplorer.core.domain.wifi.ScanWifiNetworksUseCase
import com.jsh.wirelessexplorer.core.domain.wifi.WifiRepository
import com.jsh.wirelessexplorer.core.model.TrafficSpeed
import com.jsh.wirelessexplorer.core.model.WifiConnectionInfo
import com.jsh.wirelessexplorer.core.model.WifiNetwork
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class WifiViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val scanWifiNetworks: ScanWifiNetworksUseCase = mockk(relaxed = true)
    private val observeWifiConnection: ObserveWifiConnectionUseCase = mockk(relaxed = true)
    private val observeTrafficSpeed: ObserveTrafficSpeedUseCase = mockk(relaxed = true)
    private val wifiRepository: WifiRepository = mockk(relaxed = true)

    private lateinit var viewModel: WifiViewModel

    @Before
    fun setup() {
        every { observeWifiConnection() } returns emptyFlow()
        every { observeTrafficSpeed() } returns flowOf(TrafficSpeed())
    }

    private fun createViewModel(): WifiViewModel = WifiViewModel(
        scanWifiNetworks = scanWifiNetworks,
        observeWifiConnection = observeWifiConnection,
        observeTrafficSpeed = observeTrafficSpeed,
        wifiRepository = wifiRepository,
    )

    @Test
    fun startScan_whenWifiDisabled_setsError() = runTest {
        every { wifiRepository.isWifiEnabled() } returns false
        viewModel = createViewModel()

        viewModel.startScan()

        viewModel.uiState.test {
            assertEquals("WIFI_DISABLED", awaitItem().error)
        }
    }

    @Test
    fun clearError_clearsErrorState() = runTest {
        every { wifiRepository.isWifiEnabled() } returns false
        viewModel = createViewModel()

        viewModel.startScan()
        viewModel.clearError()

        viewModel.uiState.test {
            assertNull(awaitItem().error)
        }
    }

    @Test
    fun startScan_whenWifiEnabled_collectsNetworks() = runTest {
        every { wifiRepository.isWifiEnabled() } returns true
        val networks = listOf(
            mockk<WifiNetwork>(relaxed = true),
        )
        every { scanWifiNetworks() } returns flowOf(networks)

        viewModel = createViewModel()
        viewModel.startScan()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(1, state.networks.size)
        }
    }
}

class MainDispatcherRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
