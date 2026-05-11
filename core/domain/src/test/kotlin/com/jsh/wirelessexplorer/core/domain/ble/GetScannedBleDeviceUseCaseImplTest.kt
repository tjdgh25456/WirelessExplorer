package com.jsh.wirelessexplorer.core.domain.ble

import com.jsh.wirelessexplorer.core.model.BleBondState
import com.jsh.wirelessexplorer.core.model.BleDevice
import com.jsh.wirelessexplorer.core.model.BleDeviceType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetScannedBleDeviceUseCaseImplTest {

    private lateinit var repository: FakeBleRepository
    private lateinit var useCase: GetScannedBleDeviceUseCaseImpl

    @Before
    fun setup() {
        repository = FakeBleRepository()
        useCase = GetScannedBleDeviceUseCaseImpl(repository)
    }

    @Test
    fun `invoke returns device when present in repository`() {
        val device = createTestDevice("AA:BB:CC:DD:EE:01")
        repository.scannedDevices["AA:BB:CC:DD:EE:01"] = device

        val result = useCase("AA:BB:CC:DD:EE:01")

        assertEquals(device, result)
    }

    @Test
    fun `invoke returns null when device not found`() {
        val result = useCase("FF:FF:FF:FF:FF:FF")

        assertNull(result)
    }

    private fun createTestDevice(address: String) = BleDevice(
        name = "Test Device",
        address = address,
        rssi = -60,
        deviceType = BleDeviceType.PERIPHERAL,
        bondState = BleBondState.NONE,
        lastSeen = System.currentTimeMillis(),
    )
}
