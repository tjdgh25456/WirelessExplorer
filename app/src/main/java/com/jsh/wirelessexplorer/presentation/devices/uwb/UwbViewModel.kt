package com.jsh.wirelessexplorer.presentation.devices.uwb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsh.wirelessexplorer.domain.model.UwbState
import com.jsh.wirelessexplorer.domain.repository.UwbRepository
import com.jsh.wirelessexplorer.domain.usecase.CheckUwbSupportUseCase
import com.jsh.wirelessexplorer.domain.usecase.StartUwbRangingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UwbViewModel @Inject constructor(
    private val checkUwbSupport: CheckUwbSupportUseCase,
    private val startUwbRanging: StartUwbRangingUseCase,
    private val uwbRepository: UwbRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UwbUiState())
    val uiState: StateFlow<UwbUiState> = _uiState.asStateFlow()

    private var rangingJob: Job? = null

    init {
        val supported = checkUwbSupport()
        _uiState.update { it.copy(isSupported = supported) }

        if (!supported) {
            _uiState.update { it.copy(state = UwbState.NotSupported) }
        }
    }

    fun startRanging() {
        if (rangingJob?.isActive == true) return

        _uiState.update { it.copy(state = UwbState.Searching) }

        rangingJob = viewModelScope.launch {
            startUwbRanging()
                .catch { e ->
                    Timber.e(e, "UWB 레인징 오류")
                    _uiState.update { it.copy(state = UwbState.Error(e.message ?: "Unknown error")) }
                }
                .collect { result ->
                    _uiState.update { it.copy(state = UwbState.Ranging(result)) }
                }
        }
    }

    fun stopRanging() {
        uwbRepository.stopRanging()
        rangingJob?.cancel()
        rangingJob = null
        _uiState.update { it.copy(state = UwbState.Idle) }
        Timber.d("UWB 레인징 수동 중지")
    }

    override fun onCleared() {
        super.onCleared()
        stopRanging()
    }
}
