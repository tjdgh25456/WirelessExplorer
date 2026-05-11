package com.jsh.wirelessexplorer.core.domain.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory
import com.jsh.wirelessexplorer.core.model.TechType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ToggleFavoriteUseCaseImplTest {

    private lateinit var repository: FakeDeviceHistoryRepository
    private lateinit var useCase: ToggleFavoriteUseCaseImpl

    @Before
    fun setup() {
        repository = FakeDeviceHistoryRepository()
        useCase = ToggleFavoriteUseCaseImpl(repository)
    }

    @Test
    fun invoke_whenDeviceExists_togglesFavoriteToTrue() = runTest {
        val device = createDevice(id = 1, isFavorite = false)
        repository.seed(device)

        useCase(1L)

        val updated = repository.getById(1L)
        assertTrue(updated!!.isFavorite)
    }

    @Test
    fun invoke_whenDeviceAlreadyFavorite_togglesFavoriteToFalse() = runTest {
        val device = createDevice(id = 2, isFavorite = true)
        repository.seed(device)

        useCase(2L)

        val updated = repository.getById(2L)
        assertFalse(updated!!.isFavorite)
    }

    @Test
    fun invoke_whenDeviceNotFound_doesNothing() = runTest {
        val device = createDevice(id = 1, isFavorite = false)
        repository.seed(device)

        useCase(999L)

        val unchanged = repository.getById(1L)
        assertFalse(unchanged!!.isFavorite)
    }

    private fun createDevice(id: Long, isFavorite: Boolean) = DeviceHistory(
        id = id,
        deviceName = "Device-$id",
        deviceAddress = "AA:BB:CC:DD:EE:0$id",
        techType = TechType.BLE,
        isFavorite = isFavorite,
        lastConnectedAt = 1_000_000L,
    )
}
