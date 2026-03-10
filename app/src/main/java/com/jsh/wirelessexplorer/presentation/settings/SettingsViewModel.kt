package com.jsh.wirelessexplorer.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsh.wirelessexplorer.BuildConfig
import com.jsh.wirelessexplorer.domain.model.AppTheme
import com.jsh.wirelessexplorer.domain.model.SignalUnit
import com.jsh.wirelessexplorer.domain.repository.DeviceHistoryRepository
import com.jsh.wirelessexplorer.domain.repository.SettingsRepository
import com.jsh.wirelessexplorer.domain.usecase.ObserveSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeSettings: ObserveSettingsUseCase,
    private val settingsRepository: SettingsRepository,
    private val historyRepository: DeviceHistoryRepository,
) : ViewModel() {

    private val showClearDialog = MutableStateFlow(false)

    val uiState: StateFlow<SettingsUiState> = combine(
        observeSettings(),
        showClearDialog,
    ) { settings, showDialog ->
        SettingsUiState(
            settings = settings,
            showClearHistoryDialog = showDialog,
            appVersion = BuildConfig.VERSION_NAME,
        )
    }.catch { throwable ->
        Timber.e(throwable, "설정 상태 수집 오류")
        emit(SettingsUiState(appVersion = BuildConfig.VERSION_NAME))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState(appVersion = BuildConfig.VERSION_NAME),
    )

    fun updateScanDuration(seconds: Int) = viewModelScope.launch {
        settingsRepository.updateScanDuration(seconds)
    }

    fun updateAutoScan(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.updateAutoScan(enabled)
    }

    fun updateShowUnnamed(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.updateShowUnnamed(enabled)
    }

    fun updateFilterWeakSignals(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.updateFilterWeakSignals(enabled)
    }

    fun updateRssiThreshold(threshold: Int) = viewModelScope.launch {
        settingsRepository.updateRssiThreshold(threshold)
    }

    fun updateAppTheme(theme: AppTheme) = viewModelScope.launch {
        settingsRepository.updateAppTheme(theme)
    }

    fun updateSignalUnit(unit: SignalUnit) = viewModelScope.launch {
        settingsRepository.updateSignalUnit(unit)
    }

    fun updateNewDeviceAlerts(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.updateNewDeviceAlerts(enabled)
    }

    fun updateConnectionLostAlert(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.updateConnectionLostAlert(enabled)
    }

    fun updateSignalStrengthAlert(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.updateSignalStrengthAlert(enabled)
    }

    fun showClearHistoryDialog() {
        showClearDialog.value = true
    }

    fun dismissClearHistoryDialog() {
        showClearDialog.value = false
    }

    /** 전체 기기 이력을 삭제한다. */
    fun clearAllHistory() = viewModelScope.launch {
        historyRepository.deleteAll()
        showClearDialog.value = false
        Timber.d("전체 기기 이력 삭제 완료")
    }
}
