package com.jsh.wirelessexplorer.core.domain.ble

import app.cash.turbine.test
import com.jsh.wirelessexplorer.core.common.TimeProvider
import com.jsh.wirelessexplorer.core.domain.history.FakeDeviceHistoryRepository
import com.jsh.wirelessexplorer.core.model.BleConnectionState
import com.jsh.wirelessexplorer.core.model.TechType
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class ConnectBleDeviceUseCaseImplTest {

    private lateinit var bleRepository: FakeBleRepository
    private lateinit var historyRepository: FakeDeviceHistoryRepository
    private lateinit var useCase: ConnectBleDeviceUseCaseImpl

    private val fakeTimeProvider = object : TimeProvider {
        override fun now(): Long = 1_700_000_000L
    }

    @Before
    fun setup() {
        bleRepository = FakeBleRepository()
        historyRepository = FakeDeviceHistoryRepository()
        useCase = ConnectBleDeviceUseCaseImpl(bleRepository, historyRepository, fakeTimeProvider)
    }

    @Test
    fun invoke_whenConnected_upsertsHistoryAndIncrements() = runTest {
        bleRepository.connectFlow = flowOf(
            BleConnectionState.Connecting,
            BleConnectionState.Connected(services = emptyList()),
        )

        useCase("AA:BB:CC:DD:EE:01", "Heart Rate").test {
            assertEquals(BleConnectionState.Connecting, awaitItem())
            assertEquals(BleConnectionState.Connected(services = emptyList()), awaitItem())
            awaitComplete()
        }

        val saved = historyRepository.observeAll()
        saved.test {
            val devices = awaitItem()
            assertEquals(1, devices.size)
            val device = devices.first()
            assertEquals("Heart Rate", device.deviceName)
            assertEquals("AA:BB:CC:DD:EE:01", device.deviceAddress)
            assertEquals(TechType.BLE, device.techType)
            assertEquals(1_700_000_000L, device.lastConnectedAt)
            // connectCount: upsert 시 1(기본값) → incrementConnectCount로 2
            assertEquals(2, device.connectCount)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun invoke_whenConnecting_doesNotUpsertHistory() = runTest {
        bleRepository.connectFlow = flowOf(BleConnectionState.Connecting)

        useCase("AA:BB:CC:DD:EE:02", "Thermometer").test {
            assertEquals(BleConnectionState.Connecting, awaitItem())
            awaitComplete()
        }

        historyRepository.observeAll().test {
            val devices = awaitItem()
            assertEquals(0, devices.size)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
