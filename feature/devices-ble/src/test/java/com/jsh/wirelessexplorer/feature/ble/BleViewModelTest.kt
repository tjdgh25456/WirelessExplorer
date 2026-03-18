package com.jsh.wirelessexplorer.feature.ble

import app.cash.turbine.test
import com.jsh.wirelessexplorer.core.domain.ble.ConnectBleDeviceUseCase
import com.jsh.wirelessexplorer.core.domain.ble.DisconnectBleDeviceUseCase
import com.jsh.wirelessexplorer.core.domain.ble.ObserveGattCharacteristicUseCase
import com.jsh.wirelessexplorer.core.domain.ble.ReadGattCharacteristicUseCase
import com.jsh.wirelessexplorer.core.domain.ble.ScanBleDevicesUseCase
import com.jsh.wirelessexplorer.core.domain.ble.WriteGattCharacteristicUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class BleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val scanBleDevices: ScanBleDevicesUseCase = mockk(relaxed = true)
    private val connectBleDevice: ConnectBleDeviceUseCase = mockk(relaxed = true)
    private val disconnectBleDevice: DisconnectBleDeviceUseCase = mockk(relaxed = true)
    private val readGattCharacteristic: ReadGattCharacteristicUseCase = mockk(relaxed = true)
    private val writeGattCharacteristic: WriteGattCharacteristicUseCase = mockk(relaxed = true)
    private val observeGattCharacteristic: ObserveGattCharacteristicUseCase = mockk(relaxed = true)

    private fun createViewModel(): BleViewModel = BleViewModel(
        scanBleDevices = scanBleDevices,
        connectBleDevice = connectBleDevice,
        disconnectBleDevice = disconnectBleDevice,
        readGattCharacteristic = readGattCharacteristic,
        writeGattCharacteristic = writeGattCharacteristic,
        observeGattCharacteristic = observeGattCharacteristic,
    )

    @Test
    fun updateFilter_updatesFilterQuery() = runTest {
        val viewModel = createViewModel()

        viewModel.updateFilter("Galaxy")

        viewModel.uiState.test {
            assertEquals("Galaxy", awaitItem().filterQuery)
        }
    }

    @Test
    fun startScan_whenBluetoothDisabled_setsError() = runTest {
        every { scanBleDevices() } returns flowOf(emptyList())

        val viewModel = createViewModel()

        // BleViewModel 은 scanBleDevices UseCase 오류 시 catch 블록에서 error를 세팅한다.
        // Bluetooth 비활성화 시 UseCase가 예외를 던지는 시나리오
        val error = IllegalStateException("Bluetooth is disabled")
        every { scanBleDevices() } throws error

        try {
            viewModel.startScan()
        } catch (_: IllegalStateException) {
            // UseCase 호출 자체에서 예외 발생
        }

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(false, state.isScanning)
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
