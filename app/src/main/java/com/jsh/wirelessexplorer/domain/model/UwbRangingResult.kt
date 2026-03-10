package com.jsh.wirelessexplorer.domain.model

data class UwbRangingResult(
    val distanceMeters: Float,
    val azimuthDegrees: Float,
    val elevationDegrees: Float,
    val timestamp: Long = System.currentTimeMillis(),
)
