package com.jsh.wirelessexplorer.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jsh.wirelessexplorer.di.IoDispatcher
import com.jsh.wirelessexplorer.domain.model.AppTheme
import com.jsh.wirelessexplorer.domain.model.SignalUnit
import com.jsh.wirelessexplorer.domain.model.UserSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private const val DEFAULT_SCAN_DURATION_SECONDS = 30
private const val DEFAULT_RSSI_THRESHOLD_DBM = -90

@Singleton
class SettingsDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    private object PreferencesKeys {
        val SCAN_DURATION = intPreferencesKey("scan_duration")
        val AUTO_SCAN = booleanPreferencesKey("auto_scan_on_open")
        val SHOW_UNNAMED = booleanPreferencesKey("show_unnamed_devices")
        val FILTER_WEAK = booleanPreferencesKey("filter_weak_signals")
        val RSSI_THRESHOLD = intPreferencesKey("rssi_threshold")
        val APP_THEME = stringPreferencesKey("app_theme")
        val SIGNAL_UNIT = stringPreferencesKey("signal_unit")
        val NEW_DEVICE_ALERTS = booleanPreferencesKey("new_device_alerts")
        val CONNECTION_LOST_ALERT = booleanPreferencesKey("connection_lost_alert")
        val SIGNAL_STRENGTH_ALERT = booleanPreferencesKey("signal_strength_alert")
    }

    fun observeSettings(): Flow<UserSettings> =
        dataStore.data.map { prefs ->
            UserSettings(
                scanDuration = prefs[PreferencesKeys.SCAN_DURATION] ?: DEFAULT_SCAN_DURATION_SECONDS,
                autoScanOnOpen = prefs[PreferencesKeys.AUTO_SCAN] ?: true,
                showUnnamedDevices = prefs[PreferencesKeys.SHOW_UNNAMED] ?: false,
                filterWeakSignals = prefs[PreferencesKeys.FILTER_WEAK] ?: false,
                rssiThreshold = prefs[PreferencesKeys.RSSI_THRESHOLD] ?: DEFAULT_RSSI_THRESHOLD_DBM,
                appTheme = prefs[PreferencesKeys.APP_THEME]
                    ?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() }
                    ?: AppTheme.DARK,
                signalUnit = prefs[PreferencesKeys.SIGNAL_UNIT]
                    ?.let { runCatching { SignalUnit.valueOf(it) }.getOrNull() }
                    ?: SignalUnit.DBM,
                newDeviceAlerts = prefs[PreferencesKeys.NEW_DEVICE_ALERTS] ?: true,
                connectionLostAlert = prefs[PreferencesKeys.CONNECTION_LOST_ALERT] ?: true,
                signalStrengthAlert = prefs[PreferencesKeys.SIGNAL_STRENGTH_ALERT] ?: false,
            )
        }.flowOn(ioDispatcher)

    suspend fun updateScanDuration(seconds: Int) {
        editPreference {
            it[PreferencesKeys.SCAN_DURATION] = seconds
            Timber.d("스캔 지속시간 변경: ${seconds}s")
        }
    }

    suspend fun updateAutoScan(enabled: Boolean) {
        editPreference { it[PreferencesKeys.AUTO_SCAN] = enabled }
    }

    suspend fun updateShowUnnamed(enabled: Boolean) {
        editPreference { it[PreferencesKeys.SHOW_UNNAMED] = enabled }
    }

    suspend fun updateFilterWeakSignals(enabled: Boolean) {
        editPreference { it[PreferencesKeys.FILTER_WEAK] = enabled }
    }

    suspend fun updateRssiThreshold(threshold: Int) {
        editPreference {
            it[PreferencesKeys.RSSI_THRESHOLD] = threshold
            Timber.d("RSSI 임계값 변경: ${threshold}dBm")
        }
    }

    suspend fun updateAppTheme(theme: AppTheme) {
        editPreference { it[PreferencesKeys.APP_THEME] = theme.name }
    }

    suspend fun updateSignalUnit(unit: SignalUnit) {
        editPreference { it[PreferencesKeys.SIGNAL_UNIT] = unit.name }
    }

    suspend fun updateNewDeviceAlerts(enabled: Boolean) {
        editPreference { it[PreferencesKeys.NEW_DEVICE_ALERTS] = enabled }
    }

    suspend fun updateConnectionLostAlert(enabled: Boolean) {
        editPreference { it[PreferencesKeys.CONNECTION_LOST_ALERT] = enabled }
    }

    suspend fun updateSignalStrengthAlert(enabled: Boolean) {
        editPreference { it[PreferencesKeys.SIGNAL_STRENGTH_ALERT] = enabled }
    }

    private suspend fun editPreference(block: (MutablePreferences) -> Unit) {
        withContext(ioDispatcher) { dataStore.edit(block) }
    }
}
