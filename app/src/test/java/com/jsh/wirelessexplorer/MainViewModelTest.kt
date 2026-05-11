package com.jsh.wirelessexplorer

import app.cash.turbine.test
import com.jsh.wirelessexplorer.core.domain.settings.OnboardingRepository
import com.jsh.wirelessexplorer.core.domain.settings.CheckOnboardingUseCaseImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MainViewModelTest {

    @Test
    fun uiState_whenOnboardingIncomplete_emitsIncompleteState() = runTest {
        val repository = FakeOnboardingRepository(isCompleted = false)
        val viewModel = MainViewModel(CheckOnboardingUseCaseImpl(repository))

        viewModel.uiState.test {
            val initial = awaitItem()
            assertTrue(initial.isLoading)

            val resolved = awaitItem()
            assertFalse(resolved.isLoading)
            assertFalse(resolved.isOnboardingCompleted)
        }
    }

    @Test
    fun uiState_whenOnboardingCompleted_emitsCompletedState() = runTest {
        val repository = FakeOnboardingRepository(isCompleted = true)
        val viewModel = MainViewModel(CheckOnboardingUseCaseImpl(repository))

        viewModel.uiState.test {
            val initial = awaitItem()
            assertTrue(initial.isLoading)

            val resolved = awaitItem()
            assertFalse(resolved.isLoading)
            assertTrue(resolved.isOnboardingCompleted)
        }
    }

    private class FakeOnboardingRepository(
        isCompleted: Boolean,
    ) : OnboardingRepository {
        private val completionState = MutableStateFlow(isCompleted)

        override fun isOnboardingCompleted(): Flow<Boolean> = completionState

        override suspend fun setOnboardingCompleted() {
            completionState.value = true
        }
    }
}
