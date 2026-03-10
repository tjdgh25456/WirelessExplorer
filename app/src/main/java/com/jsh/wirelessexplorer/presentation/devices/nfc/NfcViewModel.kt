package com.jsh.wirelessexplorer.presentation.devices.nfc

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsh.wirelessexplorer.domain.model.NfcState
import com.jsh.wirelessexplorer.domain.repository.NfcRepository
import com.jsh.wirelessexplorer.domain.usecase.ObserveNfcTagUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NfcViewModel @Inject constructor(
    private val observeNfcTag: ObserveNfcTagUseCase,
    private val nfcRepository: NfcRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NfcUiState())
    val uiState: StateFlow<NfcUiState> = _uiState.asStateFlow()

    init {
        checkNfcAvailability()
        observeTags()
    }

    private fun checkNfcAvailability() {
        val supported = nfcRepository.isNfcSupported()
        val enabled = nfcRepository.isNfcEnabled()

        _uiState.update { current ->
            current.copy(
                isNfcSupported = supported,
                isNfcEnabled = enabled,
                state = when {
                    !supported -> NfcState.NotSupported
                    !enabled -> NfcState.Disabled
                    else -> NfcState.Waiting
                },
            )
        }
    }

    private fun observeTags() {
        viewModelScope.launch {
            observeNfcTag().collect { tagData ->
                Timber.d("NFC 태그 수신: ${tagData.id}")
                _uiState.update { current ->
                    current.copy(state = NfcState.TagDetected(tagData))
                }
            }
        }
    }

    fun enableReader(activity: Activity) {
        if (!_uiState.value.isNfcSupported) return

        // NFC 활성화 상태 재확인
        val enabled = nfcRepository.isNfcEnabled()
        if (!enabled) {
            _uiState.update { it.copy(isNfcEnabled = false, state = NfcState.Disabled) }
            return
        }

        _uiState.update { it.copy(isNfcEnabled = true) }
        nfcRepository.enableReader(activity)
    }

    fun disableReader(activity: Activity) {
        nfcRepository.disableReader(activity)
    }

    /** 태그 스캔 후 대기 상태로 복귀한다. */
    fun resetToWaiting() {
        _uiState.update { it.copy(state = NfcState.Waiting) }
    }
}
