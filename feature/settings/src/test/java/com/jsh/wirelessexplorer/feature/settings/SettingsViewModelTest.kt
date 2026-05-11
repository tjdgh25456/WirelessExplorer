package com.jsh.wirelessexplorer.feature.settings

import app.cash.turbine.test
import com.jsh.wirelessexplorer.core.domain.history.DeviceHistoryRepository
import com.jsh.wirelessexplorer.core.domain.settings.ObserveSettingsUseCase
import com.jsh.wirelessexplorer.core.domain.settings.SettingsRepository
import com.jsh.wirelessexplorer.core.model.DeviceHistory
import com.jsh.wirelessexplorer.core.model.TechType
import com.jsh.wirelessexplorer.core.model.UserSettings
import com.jsh.wirelessexplorer.core.testing.MainDispatcherRule
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeSettings: ObserveSettingsUseCase = mockk()
    private val settingsRepository: SettingsRepository = mockk(relaxUnitFun = true)
    private val historyRepository: DeviceHistoryRepository = mockk(relaxUnitFun = true)

    private fun createViewModel(): SettingsViewModel {
        every { observeSettings() } returns flowOf(UserSettings())
        return SettingsViewModel(
            observeSettings = observeSettings,
            settingsRepository = settingsRepository,
            historyRepository = historyRepository,
        )
    }

    @Test
    fun uiState_emitsSettingsFromUseCase() = runTest {
        val settings = UserSettings(scanDuration = 60, autoScanOnOpen = false)
        every { observeSettings() } returns flowOf(settings)

        val viewModel = SettingsViewModel(
            observeSettings = observeSettings,
            settingsRepository = settingsRepository,
            historyRepository = historyRepository,
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(settings, state.settings)
            assertFalse(state.showClearHistoryDialog)
        }
    }

    @Test
    fun showClearHistoryDialog_updatesState() = runTest {
        val viewModel = createViewModel()

        viewModel.showClearHistoryDialog()

        viewModel.uiState.test {
            assertTrue(awaitItem().showClearHistoryDialog)
        }
    }

    @Test
    fun dismissClearHistoryDialog_updatesState() = runTest {
        val viewModel = createViewModel()

        viewModel.showClearHistoryDialog()
        viewModel.dismissClearHistoryDialog()

        viewModel.uiState.test {
            assertFalse(awaitItem().showClearHistoryDialog)
        }
    }

    @Test
    fun clearAllHistory_deletesAllAndDismissesDialog() = runTest {
        val viewModel = createViewModel()

        viewModel.showClearHistoryDialog()
        viewModel.clearAllHistory()

        viewModel.uiState.test {
            assertFalse(awaitItem().showClearHistoryDialog)
        }
        coVerify { historyRepository.deleteAll() }
    }

    // exportHistoryAsJson uses org.json.JSONObject (Android framework)
    // → instrumented test에서 검증
}
