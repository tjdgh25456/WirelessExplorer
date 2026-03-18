package com.jsh.wirelessexplorer.core.model

private const val DEFAULT_SCAN_DURATION_SECONDS = 30
private const val DEFAULT_RSSI_THRESHOLD_DBM = -90

data class UserSettings(
    val scanDuration: Int = DEFAULT_SCAN_DURATION_SECONDS,
    val autoScanOnOpen: Boolean = true,
    val showUnnamedDevices: Boolean = false,
    val filterWeakSignals: Boolean = false,
    val rssiThreshold: Int = DEFAULT_RSSI_THRESHOLD_DBM,
    val appTheme: AppTheme = AppTheme.DARK,
    val signalUnit: SignalUnit = SignalUnit.DBM,
    val newDeviceAlerts: Boolean = true,
    val connectionLostAlert: Boolean = true,
    val signalStrengthAlert: Boolean = false,
)
