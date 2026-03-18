package com.jsh.wirelessexplorer.core.domain.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory
import com.jsh.wirelessexplorer.core.model.TechType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetOrCreateDeviceHistoryIdUseCaseImplTest {

    private lateinit var repository: FakeDeviceHistoryRepository
    private lateinit var useCase: GetOrCreateDeviceHistoryIdUseCaseImpl

    @Before
    fun setup() {
        repository = FakeDeviceHistoryRepository()
        useCase = GetOrCreateDeviceHistoryIdUseCaseImpl(repository)
    }

    @Test
    fun invoke_whenUpsertSucceeds_returnsDeviceId() = runTest {
        val device = DeviceHistory(
            id = 42,
            deviceName = "Sensor",
            deviceAddress = "AA:BB:CC:DD:EE:01",
            techType = TechType.BLE,
            lastConnectedAt = 1_000_000L,
        )

        val result = useCase(device)

        assertTrue(result.isSuccess)
        assertEquals(42L, result.getOrThrow())
    }

    @Test
    fun invoke_whenDeviceNotFoundAfterUpsert_returnsFailure() = runTest {
        val repository = object : FakeDeviceHistoryRepository() {
            override suspend fun getById(id: Long): DeviceHistory? = null
        }
        val useCase = GetOrCreateDeviceHistoryIdUseCaseImpl(repository)

        val device = DeviceHistory(
            id = 1,
            deviceName = "Ghost",
            deviceAddress = "FF:FF:FF:FF:FF:FF",
            techType = TechType.NFC,
            lastConnectedAt = 1_000_000L,
        )

        val result = useCase(device)

        assertTrue(result.isFailure)
    }
}
