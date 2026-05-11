package com.jsh.wirelessexplorer.core.domain.history

import app.cash.turbine.test
import com.jsh.wirelessexplorer.core.model.DeviceHistory
import com.jsh.wirelessexplorer.core.model.TechType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ObserveDeviceHistoryUseCaseImplTest {

    private lateinit var repository: FakeDeviceHistoryRepository
    private lateinit var useCase: ObserveDeviceHistoryUseCaseImpl

    @Before
    fun setup() {
        repository = FakeDeviceHistoryRepository()
        useCase = ObserveDeviceHistoryUseCaseImpl(repository)
    }

    @Test
    fun invoke_withNullTechType_returnsAllDevices() = runTest {
        val bleDevice = createDevice(id = 1, techType = TechType.BLE)
        val wifiDevice = createDevice(id = 2, techType = TechType.WIFI)
        repository.seed(bleDevice, wifiDevice)

        useCase(techType = null).test {
            val result = awaitItem()
            assertEquals(2, result.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun invoke_withBleTechType_returnsOnlyBleDevices() = runTest {
        val bleDevice = createDevice(id = 1, techType = TechType.BLE)
        val wifiDevice = createDevice(id = 2, techType = TechType.WIFI)
        repository.seed(bleDevice, wifiDevice)

        useCase(techType = TechType.BLE).test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals(TechType.BLE, result.first().techType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createDevice(id: Long, techType: TechType) = DeviceHistory(
        id = id,
        deviceName = "Device-$id",
        deviceAddress = "AA:BB:CC:DD:EE:0$id",
        techType = techType,
        lastConnectedAt = 1_000_000L,
    )
}
