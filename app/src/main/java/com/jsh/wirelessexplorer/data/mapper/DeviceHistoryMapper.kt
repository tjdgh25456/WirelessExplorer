package com.jsh.wirelessexplorer.data.mapper

import com.jsh.wirelessexplorer.data.local.entity.DeviceHistoryEntity
import com.jsh.wirelessexplorer.domain.model.DeviceHistory
import com.jsh.wirelessexplorer.domain.model.TechType

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
