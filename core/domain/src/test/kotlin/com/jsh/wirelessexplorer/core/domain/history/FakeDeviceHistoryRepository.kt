package com.jsh.wirelessexplorer.core.domain.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory
import com.jsh.wirelessexplorer.core.model.TechType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

open class FakeDeviceHistoryRepository : DeviceHistoryRepository {

    private val devices = MutableStateFlow<List<DeviceHistory>>(emptyList())

    fun seed(vararg items: DeviceHistory) {
        devices.value = items.toList()
    }

    override fun observeAll(): Flow<List<DeviceHistory>> = devices

    override fun observeByTechType(techType: TechType): Flow<List<DeviceHistory>> =
        devices.map { list -> list.filter { it.techType == techType } }

    override fun search(query: String): Flow<List<DeviceHistory>> =
        devices.map { list ->
            list.filter { it.deviceName.contains(query, ignoreCase = true) }
        }

    override suspend fun getById(id: Long): DeviceHistory? =
        devices.value.find { it.id == id }

    override suspend fun upsert(device: DeviceHistory) {
        devices.update { list ->
            val index = list.indexOfFirst { it.id == device.id }
            if (index >= 0) list.toMutableList().apply { set(index, device) }
            else list + device
        }
    }

    override suspend fun updateFavorite(id: Long, isFavorite: Boolean) {
        devices.update { list ->
            list.map { if (it.id == id) it.copy(isFavorite = isFavorite) else it }
        }
    }

    override suspend fun incrementConnectCount(address: String, techType: TechType) {
        devices.update { list ->
            list.map {
                if (it.deviceAddress == address && it.techType == techType)
                    it.copy(connectCount = it.connectCount + 1)
                else it
            }
        }
    }

    override suspend fun delete(device: DeviceHistory) {
        devices.update { list -> list.filter { it.id != device.id } }
    }

    override suspend fun deleteAll() {
        devices.value = emptyList()
    }

    override fun observeDeviceCount(): Flow<Int> = devices.map { it.size }

    override fun observeTotalConnections(): Flow<Int?> =
        devices.map { list -> list.sumOf { it.connectCount }.takeIf { list.isNotEmpty() } }

    override fun observeBestSignal(): Flow<Int?> =
        devices.map { list -> list.mapNotNull { it.rssi }.maxOrNull() }

    override fun observeById(id: Long): Flow<DeviceHistory?> =
        devices.map { list -> list.find { it.id == id } }
}
