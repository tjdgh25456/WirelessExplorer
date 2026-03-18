package com.jsh.wirelessexplorer.core.model

data class UwbRangingResult(
    val distanceMeters: Float,
    val azimuthDegrees: Float,
    val elevationDegrees: Float,
    val timestamp: Long,
)
