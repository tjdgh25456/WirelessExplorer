package com.jsh.wirelessexplorer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.jsh.wirelessexplorer.data.local.entity.DeviceHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceHistoryDao {

    @Query("SELECT * FROM device_history ORDER BY last_connected_at DESC")
    fun observeAll(): Flow<List<DeviceHistoryEntity>>

    @Query("SELECT * FROM device_history WHERE tech_type = :techType ORDER BY last_connected_at DESC")
    fun observeByTechType(techType: String): Flow<List<DeviceHistoryEntity>>

    @Query(
        """
        SELECT * FROM device_history
        WHERE device_name LIKE '%' || :query || '%'
           OR device_address LIKE '%' || :query || '%'
        ORDER BY last_connected_at DESC
        """
    )
    fun search(query: String): Flow<List<DeviceHistoryEntity>>

    @Query("SELECT * FROM device_history WHERE id = :id")
    suspend fun getById(id: Long): DeviceHistoryEntity?

    @Upsert
    suspend fun upsert(entity: DeviceHistoryEntity)

    @Query("UPDATE device_history SET is_favorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    @Query(
        """
        UPDATE device_history
        SET connect_count = connect_count + 1, last_connected_at = :timestamp
        WHERE device_address = :address AND tech_type = :techType
        """
    )
    suspend fun incrementConnectCount(
        address: String,
        techType: String,
        timestamp: Long = System.currentTimeMillis()
    )

    @Delete
    suspend fun delete(entity: DeviceHistoryEntity)

    @Query("DELETE FROM device_history")
    suspend fun deleteAll()

    @Query("SELECT COUNT(DISTINCT device_address) FROM device_history")
    fun observeDeviceCount(): Flow<Int>

    @Query("SELECT SUM(connect_count) FROM device_history")
    fun observeTotalConnections(): Flow<Int?>

    @Query("SELECT MAX(rssi) FROM device_history")
    fun observeBestSignal(): Flow<Int?>
}
