package com.jsh.wirelessexplorer.data.repository

import com.jsh.wirelessexplorer.data.datasource.SettingsDataSource
import com.jsh.wirelessexplorer.domain.model.AppTheme
import com.jsh.wirelessexplorer.domain.model.SignalUnit
import com.jsh.wirelessexplorer.domain.model.UserSettings
import com.jsh.wirelessexplorer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataSource: SettingsDataSource,
) : SettingsRepository {

    override fun observeSettings(): Flow<UserSettings> = dataSource.observeSettings()

    override suspend fun updateScanDuration(seconds: Int) = dataSource.updateScanDuration(seconds)

    override suspend fun updateAutoScan(enabled: Boolean) = dataSource.updateAutoScan(enabled)

    override suspend fun updateShowUnnamed(enabled: Boolean) = dataSource.updateShowUnnamed(enabled)

    override suspend fun updateFilterWeakSignals(enabled: Boolean) =
        dataSource.updateFilterWeakSignals(enabled)

    override suspend fun updateRssiThreshold(threshold: Int) =
        dataSource.updateRssiThreshold(threshold)

    override suspend fun updateAppTheme(theme: AppTheme) = dataSource.updateAppTheme(theme)

    override suspend fun updateSignalUnit(unit: SignalUnit) = dataSource.updateSignalUnit(unit)

    override suspend fun updateNewDeviceAlerts(enabled: Boolean) =
        dataSource.updateNewDeviceAlerts(enabled)

    override suspend fun updateConnectionLostAlert(enabled: Boolean) =
        dataSource.updateConnectionLostAlert(enabled)

    override suspend fun updateSignalStrengthAlert(enabled: Boolean) =
        dataSource.updateSignalStrengthAlert(enabled)
}
