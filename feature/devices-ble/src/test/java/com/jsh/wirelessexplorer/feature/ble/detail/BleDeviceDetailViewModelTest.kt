package com.jsh.wirelessexplorer.feature.ble.detail

import androidx.lifecycle.SavedStateHandle
import com.jsh.wirelessexplorer.core.domain.ble.ConnectBleDeviceUseCase
import com.jsh.wirelessexplorer.core.domain.ble.DisconnectBleDeviceUseCase
import com.jsh.wirelessexplorer.core.domain.ble.GetScannedBleDeviceUseCase
import com.jsh.wirelessexplorer.core.domain.ble.ObserveGattCharacteristicUseCase
import com.jsh.wirelessexplorer.core.domain.ble.ReadGattCharacteristicUseCase
import com.jsh.wirelessexplorer.core.domain.ble.WriteGattCharacteristicUseCase
import com.jsh.wirelessexplorer.core.model.BleBondState
import com.jsh.wirelessexplorer.core.model.BleConnectionState
import com.jsh.wirelessexplorer.core.model.BleDevice
import com.jsh.wirelessexplorer.core.model.BleDeviceType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BleDeviceDetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testDevice = BleDevice(
        name = "Test Device",
        address = "AA:BB:CC:DD:EE:01",
        rssi = -55,
        deviceType = BleDeviceType.PERIPHERAL,
        bondState = BleBondState.NONE,
        lastSeen = System.currentTimeMillis(),
    )

    private val getScannedBleDevice: GetScannedBleDeviceUseCase = mockk()
    private val connectBleDevice: ConnectBleDeviceUseCase = mockk()
    private val disconnectBleDevice: DisconnectBleDeviceUseCase = mockk(relaxed = true)
    private val readGattCharacteristic: ReadGattCharacteristicUseCase = mockk()
    private val writeGattCharacteristic: WriteGattCharacteristicUseCase = mockk()
    private val observeGattCharacteristic: ObserveGattCharacteristicUseCase = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): BleDeviceDetailViewModel {
        val savedStateHandle = SavedStateHandle(
            mapOf("address" to testDevice.address),
        )
        return BleDeviceDetailViewModel(
            savedStateHandle = savedStateHandle,
            getScannedBleDevice = getScannedBleDevice,
            connectBleDevice = connectBleDevice,
            disconnectBleDevice = disconnectBleDevice,
            readGattCharacteristic = readGattCharacteristic,
            writeGattCharacteristic = writeGattCharacteristic,
            observeGattCharacteristic = observeGattCharacteristic,
        )
    }

    @Test
    fun `init loads device from scanner cache`() {
        every { getScannedBleDevice(testDevice.address) } returns testDevice

        val viewModel = createViewModel()

        assertNotNull(viewModel.uiState.value.device)
        assertEquals(testDevice.address, viewModel.uiState.value.device?.address)
    }

    @Test
    fun `init handles missing device gracefully`() {
        every { getScannedBleDevice(testDevice.address) } returns null

        val viewModel = createViewModel()

        assertNull(viewModel.uiState.value.device)
    }

    @Test
    fun `connect updates connection state`() {
        every { getScannedBleDevice(testDevice.address) } returns testDevice
        every { connectBleDevice(testDevice.address, testDevice.name) } returns
            flowOf(BleConnectionState.Connecting, BleConnectionState.Connected(emptyList()))

        val viewModel = createViewModel()
        viewModel.connect()

        assertEquals(
            BleConnectionState.Connected(emptyList()),
            viewModel.uiState.value.connectionState,
        )
    }

    @Test
    fun `disconnect resets connection state`() {
        every { getScannedBleDevice(testDevice.address) } returns testDevice

        val viewModel = createViewModel()
        viewModel.disconnect()

        assertEquals(BleConnectionState.Idle, viewModel.uiState.value.connectionState)
        verify { disconnectBleDevice() }
    }

    @Test
    fun `clearError sets error to null`() {
        every { getScannedBleDevice(testDevice.address) } returns testDevice

        val viewModel = createViewModel()
        viewModel.clearError()

        assertNull(viewModel.uiState.value.error)
    }
}
