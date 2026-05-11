package com.jsh.wirelessexplorer.core.data.ble

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.jsh.wirelessexplorer.core.common.TimeProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BleScannerTest {

    private val context: Context = mockk(relaxed = true)
    private val timeProvider: TimeProvider = mockk { every { now() } returns 1000L }

    private fun createScanner(adapter: BluetoothAdapter? = null) =
        BleScanner(context, adapter, timeProvider)

    @Test
    fun isBluetoothEnabled_whenAdapterNull_returnsFalse() {
        val scanner = createScanner(adapter = null)
        assertFalse(scanner.isBluetoothEnabled())
    }

    @Test
    fun isBluetoothEnabled_whenDisabled_returnsFalse() {
        val adapter: BluetoothAdapter = mockk { every { isEnabled } returns false }
        val scanner = createScanner(adapter)
        assertFalse(scanner.isBluetoothEnabled())
    }

    @Test
    fun isBluetoothEnabled_whenEnabled_returnsTrue() {
        val adapter: BluetoothAdapter = mockk { every { isEnabled } returns true }
        val scanner = createScanner(adapter)
        assertTrue(scanner.isBluetoothEnabled())
    }

    @Test
    fun getDevice_whenNotScanned_returnsNull() {
        val scanner = createScanner()
        assertNull(scanner.getDevice("AA:BB:CC:DD:EE:FF"))
    }
}
