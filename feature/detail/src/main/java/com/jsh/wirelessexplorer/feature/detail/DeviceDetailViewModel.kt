package com.jsh.wirelessexplorer.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsh.wirelessexplorer.core.domain.history.DeleteDeviceHistoryUseCase
import com.jsh.wirelessexplorer.core.domain.history.ObserveDeviceDetailUseCase
import com.jsh.wirelessexplorer.core.domain.history.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DeviceDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeDeviceDetail: ObserveDeviceDetailUseCase,
    private val deleteDeviceHistory: DeleteDeviceHistoryUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {

    private val deviceId: Long = checkNotNull(savedStateHandle["deviceId"])

    private val _uiState = MutableStateFlow(DeviceDetailUiState())
    val uiState: StateFlow<DeviceDetailUiState> = _uiState.asStateFlow()

    init {
        observeDevice()
    }

    private fun observeDevice() {
        viewModelScope.launch {
            observeDeviceDetail(deviceId).collect { device ->
                _uiState.update {
                    it.copy(device = device, isLoading = false)
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            toggleFavorite.invoke(deviceId)
        }
    }

    suspend fun deleteDevice(): Boolean {
        val device = _uiState.value.device ?: return false
        return runCatching {
            deleteDeviceHistory(device)
            Timber.d("디바이스 삭제 완료: ${device.deviceName}")
            true
        }.getOrElse { e ->
            Timber.e(e, "디바이스 삭제 실패")
            false
        }
    }
}
