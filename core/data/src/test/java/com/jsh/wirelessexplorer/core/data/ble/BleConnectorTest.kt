package com.jsh.wirelessexplorer.core.data.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import com.jsh.wirelessexplorer.core.model.BleConnectionState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BleConnectorTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val context: Context = mockk(relaxed = true)
    private val bluetoothAdapter: BluetoothAdapter = mockk(relaxed = true)

    private fun createConnector(adapter: BluetoothAdapter? = bluetoothAdapter) =
        BleConnector(context, testDispatcher, adapter)

    @Test
    fun connect_whenBluetoothAdapterNull_emitsError() {
        val connector = createConnector(adapter = null)
        connector.connect("AA:BB:CC:DD:EE:FF")
        val state = connector.connectionState.value
        assertTrue(state is BleConnectionState.Error)
    }

    @Test
    fun connect_whenDeviceNotFound_emitsError() {
        every { bluetoothAdapter.getRemoteDevice("AA:BB:CC:DD:EE:FF") } returns null
        val connector = createConnector()
        connector.connect("AA:BB:CC:DD:EE:FF")
        val state = connector.connectionState.value
        assertTrue(state is BleConnectionState.Error)
    }

    @Test
    fun disconnect_resetsToIdleState() {
        val connector = createConnector()
        connector.disconnect()
        assertEquals(BleConnectionState.Idle, connector.connectionState.value)
    }

    @Test
    fun readCharacteristic_whenNotConnected_returnsFailure() = runTest {
        val connector = createConnector()
        val result = connector.readCharacteristic("service-uuid", "char-uuid")
        assertTrue(result.isFailure)
    }

    @Test
    fun writeCharacteristic_whenNotConnected_returnsFailure() = runTest {
        val connector = createConnector()
        val result = connector.writeCharacteristic("service-uuid", "char-uuid", byteArrayOf(0x01))
        assertTrue(result.isFailure)
    }

    @Test
    fun setNotification_whenNotConnected_returnsFailure() = runTest {
        val connector = createConnector()
        val result = connector.setNotification("service-uuid", "char-uuid", true)
        assertTrue(result.isFailure)
    }

    @Test
    fun connectedDevicesFlow_initiallyEmpty() {
        val connector = createConnector()
        assertTrue(connector.connectedDevicesFlow.value.isEmpty())
    }

    @Test
    fun connectionState_initiallyIdle() {
        val connector = createConnector()
        assertEquals(BleConnectionState.Idle, connector.connectionState.value)
    }
}
