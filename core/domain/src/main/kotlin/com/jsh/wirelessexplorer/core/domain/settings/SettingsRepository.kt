package com.jsh.wirelessexplorer.core.domain.settings

import com.jsh.wirelessexplorer.core.model.AppTheme
import com.jsh.wirelessexplorer.core.model.SignalUnit
import com.jsh.wirelessexplorer.core.model.UserSettings
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
