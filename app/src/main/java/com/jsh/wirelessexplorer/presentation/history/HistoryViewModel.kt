package com.jsh.wirelessexplorer.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsh.wirelessexplorer.domain.model.DeviceHistory
import com.jsh.wirelessexplorer.domain.model.TechType
import com.jsh.wirelessexplorer.domain.usecase.DeleteAllHistoryUseCase
import com.jsh.wirelessexplorer.domain.usecase.DeleteDeviceHistoryUseCase
import com.jsh.wirelessexplorer.domain.usecase.ObserveDeviceHistoryUseCase
import com.jsh.wirelessexplorer.domain.usecase.RestoreDeviceHistoryUseCase
import com.jsh.wirelessexplorer.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val observeDeviceHistory: ObserveDeviceHistoryUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val deleteDeviceHistory: DeleteDeviceHistoryUseCase,
    private val deleteAllHistory: DeleteAllHistoryUseCase,
    private val restoreDeviceHistory: RestoreDeviceHistoryUseCase,
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow<TechType?>(null)

    /** 마지막으로 삭제된 디바이스 (Undo 용도) */
    private var lastDeleted: DeviceHistory? = null

    val uiState: StateFlow<HistoryUiState> = _selectedFilter
        .flatMapLatest { filter ->
            observeDeviceHistory(filter).map { devices ->
                // 즐겨찾기를 상단으로 정렬
                val sorted = devices.sortedByDescending { it.isFavorite }
                HistoryUiState(
                    devices = sorted,
                    selectedFilter = filter,
                    isLoading = false,
                    isEmpty = sorted.isEmpty(),
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HistoryUiState(),
        )

    fun selectFilter(techType: TechType?) {
        _selectedFilter.value = techType
        Timber.d("필터 변경: ${techType?.label ?: "ALL"}")
    }

    fun toggleFavorite(deviceId: Long) {
        viewModelScope.launch {
            toggleFavorite.invoke(deviceId)
        }
    }

    fun deleteDevice(device: DeviceHistory) {
        lastDeleted = device
        viewModelScope.launch {
            deleteDeviceHistory(device)
            Timber.d("디바이스 삭제: ${device.deviceName}")
        }
    }

    /** Snackbar Undo 선택 시 삭제된 디바이스를 복원한다. */
    fun undoDelete() {
        val device = lastDeleted ?: return
        lastDeleted = null
        viewModelScope.launch {
            restoreDeviceHistory(device)
            Timber.d("삭제 취소: ${device.deviceName}")
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            deleteAllHistory()
            Timber.d("전체 히스토리 삭제")
        }
    }
}
