package com.jsh.wirelessexplorer.feature.history

import app.cash.turbine.test
import com.jsh.wirelessexplorer.core.domain.history.DeleteAllHistoryUseCase
import com.jsh.wirelessexplorer.core.domain.history.DeleteDeviceHistoryUseCase
import com.jsh.wirelessexplorer.core.domain.history.ObserveDeviceHistoryUseCase
import com.jsh.wirelessexplorer.core.domain.history.RestoreDeviceHistoryUseCase
import com.jsh.wirelessexplorer.core.domain.history.ToggleFavoriteUseCase
import com.jsh.wirelessexplorer.core.model.DeviceHistory
import com.jsh.wirelessexplorer.core.model.TechType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeDeviceHistory: ObserveDeviceHistoryUseCase = mockk()
    private val toggleFavorite: ToggleFavoriteUseCase = mockk(relaxUnitFun = true)
    private val deleteDeviceHistory: DeleteDeviceHistoryUseCase = mockk(relaxUnitFun = true)
    private val deleteAllHistory: DeleteAllHistoryUseCase = mockk(relaxUnitFun = true)
    private val restoreDeviceHistory: RestoreDeviceHistoryUseCase = mockk(relaxUnitFun = true)

    private val sampleDevices = listOf(
        DeviceHistory(
            id = 1,
            deviceName = "BLE-Sensor",
            deviceAddress = "AA:BB:CC:DD:EE:01",
            techType = TechType.BLE,
            rssi = -50,
            isFavorite = false,
            lastConnectedAt = 1_700_000_000L,
        ),
        DeviceHistory(
            id = 2,
            deviceName = "WiFi-AP",
            deviceAddress = "AA:BB:CC:DD:EE:02",
            techType = TechType.WIFI,
            rssi = -40,
            isFavorite = true,
            lastConnectedAt = 1_700_001_000L,
        ),
    )

    private fun createViewModel(): HistoryViewModel {
        every { observeDeviceHistory(any()) } returns flowOf(sampleDevices)
        return HistoryViewModel(
            observeDeviceHistory = observeDeviceHistory,
            toggleFavorite = toggleFavorite,
            deleteDeviceHistory = deleteDeviceHistory,
            deleteAllHistory = deleteAllHistory,
            restoreDeviceHistory = restoreDeviceHistory,
        )
    }

    @Test
    fun uiState_initialState_hasAllFilter() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertNull(state.selectedFilter)
            assertFalse(state.isLoading)
            assertFalse(state.isEmpty)
        }
    }

    @Test
    fun selectFilter_updatesTechTypeFilter() = runTest {
        every { observeDeviceHistory(TechType.BLE) } returns flowOf(
            sampleDevices.filter { it.techType == TechType.BLE },
        )
        val viewModel = createViewModel()

        viewModel.selectFilter(TechType.BLE)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(TechType.BLE, state.selectedFilter)
        }
    }

    @Test
    fun deleteDevice_callsDeleteUseCase() = runTest {
        val viewModel = createViewModel()
        val target = sampleDevices.first()

        viewModel.deleteDevice(target)

        coVerify { deleteDeviceHistory(target) }
    }

    @Test
    fun toggleFavorite_callsToggleUseCase() = runTest {
        val viewModel = createViewModel()

        viewModel.toggleFavorite(1L)

        coVerify { toggleFavorite(1L) }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: kotlinx.coroutines.test.TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
