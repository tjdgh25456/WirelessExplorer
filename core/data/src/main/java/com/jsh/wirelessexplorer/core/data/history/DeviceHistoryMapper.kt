package com.jsh.wirelessexplorer.core.data.history

import com.jsh.wirelessexplorer.core.database.entity.DeviceHistoryEntity
import com.jsh.wirelessexplorer.core.model.DeviceHistory
import com.jsh.wirelessexplorer.core.model.TechType

internal fun DeviceHistoryEntity.toDomain() = DeviceHistory(
    id = id,
    deviceName = deviceName,
    deviceAddress = deviceAddress,
    techType = TechType.valueOf(techType),
    rssi = rssi,
    isFavorite = isFavorite,
    connectCount = connectCount,
    lastConnectedAt = lastConnectedAt,
    extraJson = extraJson
)

internal fun DeviceHistory.toEntity() = DeviceHistoryEntity(
    id = id,
    deviceName = deviceName,
    deviceAddress = deviceAddress,
    techType = techType.name,
    rssi = rssi,
    isFavorite = isFavorite,
    connectCount = connectCount,
    lastConnectedAt = lastConnectedAt,
    extraJson = extraJson
)
