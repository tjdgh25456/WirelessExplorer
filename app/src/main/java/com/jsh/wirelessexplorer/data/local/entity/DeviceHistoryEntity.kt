package com.jsh.wirelessexplorer.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "device_history",
    indices = [Index(value = ["device_address", "tech_type"], unique = true)]
)
data class DeviceHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "device_name") val deviceName: String,
    @ColumnInfo(name = "device_address") val deviceAddress: String,
    @ColumnInfo(name = "tech_type") val techType: String,
    val rssi: Int? = null,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false,
    @ColumnInfo(name = "connect_count") val connectCount: Int = 1,
    @ColumnInfo(name = "last_connected_at") val lastConnectedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "extra_json") val extraJson: String? = null
)
