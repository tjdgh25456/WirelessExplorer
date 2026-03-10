package com.jsh.wirelessexplorer.data.repository

import com.jsh.wirelessexplorer.data.local.dao.DeviceHistoryDao
import com.jsh.wirelessexplorer.data.mapper.toDomain
import com.jsh.wirelessexplorer.data.mapper.toEntity
import com.jsh.wirelessexplorer.di.IoDispatcher
import com.jsh.wirelessexplorer.domain.model.DeviceHistory
import com.jsh.wirelessexplorer.domain.model.TechType
import com.jsh.wirelessexplorer.domain.repository.DeviceHistoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeviceHistoryRepositoryImpl @Inject constructor(
    private val dao: DeviceHistoryDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DeviceHistoryRepository {

    override fun observeAll(): Flow<List<DeviceHistory>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }.flowOn(ioDispatcher)

    override fun observeByTechType(techType: TechType): Flow<List<DeviceHistory>> =
        dao.observeByTechType(techType.name)
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(ioDispatcher)

    override fun search(query: String): Flow<List<DeviceHistory>> =
        dao.search(query).map { entities -> entities.map { it.toDomain() } }.flowOn(ioDispatcher)

    override suspend fun getById(id: Long): DeviceHistory? = withContext(ioDispatcher) {
        dao.getById(id)?.toDomain()
    }

    override suspend fun upsert(device: DeviceHistory) = withContext(ioDispatcher) {
        dao.upsert(device.toEntity())
    }

    override suspend fun updateFavorite(id: Long, isFavorite: Boolean) = withContext(ioDispatcher) {
        dao.updateFavorite(id, isFavorite)
    }

    override suspend fun incrementConnectCount(address: String, techType: TechType) =
        withContext(ioDispatcher) {
            dao.incrementConnectCount(address, techType.name)
        }

    override suspend fun delete(device: DeviceHistory) = withContext(ioDispatcher) {
        dao.delete(device.toEntity())
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        dao.deleteAll()
    }

    override fun observeDeviceCount(): Flow<Int> =
        dao.observeDeviceCount().flowOn(ioDispatcher)

    override fun observeTotalConnections(): Flow<Int?> =
        dao.observeTotalConnections().flowOn(ioDispatcher)

    override fun observeBestSignal(): Flow<Int?> =
        dao.observeBestSignal().flowOn(ioDispatcher)
}
