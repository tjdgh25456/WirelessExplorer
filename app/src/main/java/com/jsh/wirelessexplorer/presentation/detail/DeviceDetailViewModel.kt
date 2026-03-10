package com.jsh.wirelessexplorer.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsh.wirelessexplorer.domain.usecase.DeleteDeviceHistoryUseCase
import com.jsh.wirelessexplorer.domain.usecase.GetDeviceDetailUseCase
import com.jsh.wirelessexplorer.domain.usecase.ToggleFavoriteUseCase
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
    private val getDeviceDetail: GetDeviceDetailUseCase,
    private val deleteDeviceHistory: DeleteDeviceHistoryUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {

    private val deviceId: Long = checkNotNull(savedStateHandle["deviceId"])

    private val _uiState = MutableStateFlow(DeviceDetailUiState())
    val uiState: StateFlow<DeviceDetailUiState> = _uiState.asStateFlow()

    init {
        loadDevice()
    }

    private fun loadDevice() {
        viewModelScope.launch {
            val device = getDeviceDetail(deviceId)
            _uiState.update {
                it.copy(device = device, isLoading = false)
            }
            Timber.d("디바이스 상세 로드: ${device?.deviceName ?: "not found"}")
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            toggleFavorite.invoke(deviceId)
            // 변경 후 다시 로드
            loadDevice()
        }
    }

    /** 디바이스 히스토리를 삭제한다. 삭제 완료 후 true를 반환한다. */
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
