package com.jsh.wirelessexplorer.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsh.wirelessexplorer.domain.usecase.CompleteOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboarding: CompleteOnboardingUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun setCurrentPage(page: Int) {
        _uiState.update { it.copy(currentPage = page) }
    }

    /** 온보딩 완료를 저장하고 상태를 갱신한다. */
    fun completeOnboarding() {
        viewModelScope.launch {
            completeOnboarding.invoke()
            _uiState.update { it.copy(isCompleted = true) }
            Timber.d("온보딩 프로세스 완료")
        }
    }
}
