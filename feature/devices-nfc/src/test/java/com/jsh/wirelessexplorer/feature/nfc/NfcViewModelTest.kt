package com.jsh.wirelessexplorer.feature.nfc

import app.cash.turbine.test
import com.jsh.wirelessexplorer.core.data.nfc.NfcReaderController
import com.jsh.wirelessexplorer.core.domain.nfc.NfcRepository
import com.jsh.wirelessexplorer.core.domain.nfc.ObserveNfcTagUseCase
import com.jsh.wirelessexplorer.core.model.NfcState
import com.jsh.wirelessexplorer.core.testing.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NfcViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeNfcTag: ObserveNfcTagUseCase = mockk()
    private val nfcRepository: NfcRepository = mockk()
    private val nfcReaderController: NfcReaderController = mockk(relaxUnitFun = true)

    private fun createViewModel(
        supported: Boolean = true,
        enabled: Boolean = true,
    ): NfcViewModel {
        every { nfcRepository.isNfcSupported() } returns supported
        every { nfcRepository.isNfcEnabled() } returns enabled
        every { observeNfcTag() } returns emptyFlow()
        return NfcViewModel(
            observeNfcTag = observeNfcTag,
            nfcRepository = nfcRepository,
            nfcReaderController = nfcReaderController,
        )
    }

    @Test
    fun initialState_isWaiting() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(NfcState.Waiting, state.state)
            assertTrue(state.isNfcSupported)
            assertTrue(state.isNfcEnabled)
            assertFalse(state.showRawHex)
        }
    }

    @Test
    fun toggleRawHex_togglesState() = runTest {
        val viewModel = createViewModel()

        viewModel.toggleRawHex()

        viewModel.uiState.test {
            assertTrue(awaitItem().showRawHex)
        }

        viewModel.toggleRawHex()

        viewModel.uiState.test {
            assertFalse(awaitItem().showRawHex)
        }
    }

    @Test
    fun resetToWaiting_resetsState() = runTest {
        val viewModel = createViewModel()

        viewModel.resetToWaiting()

        viewModel.uiState.test {
            assertEquals(NfcState.Waiting, awaitItem().state)
        }
    }
}
