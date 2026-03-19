package com.jsh.wirelessexplorer.feature.dashboard

import app.cash.turbine.test
import com.jsh.wirelessexplorer.core.domain.ble.ObserveBleConnectionsUseCase
import com.jsh.wirelessexplorer.core.domain.dashboard.ObserveDashboardStatsUseCase
import com.jsh.wirelessexplorer.core.domain.dashboard.ObserveTrafficSpeedUseCase
import com.jsh.wirelessexplorer.core.domain.wifi.ObserveWifiStatusUseCase
import com.jsh.wirelessexplorer.core.model.BleConnectedDevice
import com.jsh.wirelessexplorer.core.model.DashboardStats
import com.jsh.wirelessexplorer.core.model.TrafficSpeed
import com.jsh.wirelessexplorer.core.model.WifiSecurityType
import com.jsh.wirelessexplorer.core.model.WifiStatus
import com.jsh.wirelessexplorer.core.testing.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeWifiStatus: ObserveWifiStatusUseCase = mockk()
    private val observeDashboardStats: ObserveDashboardStatsUseCase = mockk()
    private val observeBleConnections: ObserveBleConnectionsUseCase = mockk()
    private val observeTrafficSpeed: ObserveTrafficSpeedUseCase = mockk()

    private fun createViewModel() = DashboardViewModel(
        observeWifiStatus = observeWifiStatus,
        observeDashboardStats = observeDashboardStats,
        observeBleConnections = observeBleConnections,
        observeTrafficSpeed = observeTrafficSpeed,
    )

    @Test
    fun uiState_combinesAllFlows_emitsCorrectState() = runTest {
        val wifiStatus = WifiStatus(
            ssid = "TestNetwork",
            bssid = "AA:BB:CC:DD:EE:FF",
            rssi = -45,
            linkSpeed = 300,
            frequency = 5180,
            ipAddress = "192.168.1.10",
            securityType = WifiSecurityType.WPA3,
            channel = 36,
            isConnected = true,
        )
        val stats = DashboardStats(deviceCount = 5, totalConnections = 12, bestSignal = -30)
        val bleDevices = listOf(BleConnectedDevice(address = "11:22:33:44:55:66", name = "Sensor"))
        val speed = TrafficSpeed(downloadBytesPerSec = 1_024_000, uploadBytesPerSec = 512_000)

        every { observeWifiStatus() } returns flowOf(wifiStatus)
        every { observeDashboardStats() } returns flowOf(stats)
        every { observeBleConnections() } returns flowOf(bleDevices)
        every { observeTrafficSpeed() } returns flowOf(speed)

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(wifiStatus, state.wifiStatus)
            assertEquals(stats, state.stats)
            assertEquals(bleDevices, state.bleConnectedDevices)
            assertEquals(speed, state.trafficSpeed)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun uiState_whenWifiStatusNull_showsNotConnected() = runTest {
        every { observeWifiStatus() } returns flowOf(null)
        every { observeDashboardStats() } returns flowOf(DashboardStats())
        every { observeBleConnections() } returns flowOf(emptyList())
        every { observeTrafficSpeed() } returns flowOf(TrafficSpeed())

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertNull(state.wifiStatus)
            assertFalse(state.isLoading)
        }
    }
}
