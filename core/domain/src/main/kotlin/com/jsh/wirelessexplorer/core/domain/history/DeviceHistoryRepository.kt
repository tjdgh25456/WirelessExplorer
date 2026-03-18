package com.jsh.wirelessexplorer.core.domain.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory
import com.jsh.wirelessexplorer.core.model.TechType
import kotlinx.coroutines.flow.Flow

interface DeviceHistoryRepository {

    fun observeAll(): Flow<List<DeviceHistory>>

    fun observeByTechType(techType: TechType): Flow<List<DeviceHistory>>

    fun search(query: String): Flow<List<DeviceHistory>>

    suspend fun getById(id: Long): DeviceHistory?

    suspend fun upsert(device: DeviceHistory)

    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    suspend fun incrementConnectCount(address: String, techType: TechType)

    suspend fun delete(device: DeviceHistory)

    suspend fun deleteAll()

    fun observeDeviceCount(): Flow<Int>

    fun observeTotalConnections(): Flow<Int?>

    fun observeBestSignal(): Flow<Int?>

    fun observeById(id: Long): Flow<DeviceHistory?>
}
