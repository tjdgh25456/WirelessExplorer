package com.jsh.wirelessexplorer.domain.repository

import com.jsh.wirelessexplorer.domain.model.AppTheme
import com.jsh.wirelessexplorer.domain.model.SignalUnit
import com.jsh.wirelessexplorer.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeSettings(): Flow<UserSettings>
    suspend fun updateScanDuration(seconds: Int)
    suspend fun updateAutoScan(enabled: Boolean)
    suspend fun updateShowUnnamed(enabled: Boolean)
    suspend fun updateFilterWeakSignals(enabled: Boolean)
    suspend fun updateRssiThreshold(threshold: Int)
    suspend fun updateAppTheme(theme: AppTheme)
    suspend fun updateSignalUnit(unit: SignalUnit)
    suspend fun updateNewDeviceAlerts(enabled: Boolean)
    suspend fun updateConnectionLostAlert(enabled: Boolean)
    suspend fun updateSignalStrengthAlert(enabled: Boolean)
}
