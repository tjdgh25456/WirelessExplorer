package com.jsh.wirelessexplorer.feature.search

import com.jsh.wirelessexplorer.core.domain.history.SearchDeviceHistoryUseCase
import com.jsh.wirelessexplorer.core.domain.search.ClearRecentSearchesUseCase
import com.jsh.wirelessexplorer.core.domain.search.DeleteRecentSearchUseCase
import com.jsh.wirelessexplorer.core.domain.search.ObserveRecentSearchesUseCase
import com.jsh.wirelessexplorer.core.domain.search.SaveRecentSearchUseCase
import com.jsh.wirelessexplorer.core.testing.MainDispatcherRule
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val searchDeviceHistory: SearchDeviceHistoryUseCase = mockk(relaxed = true)
    private val observeRecentSearches: ObserveRecentSearchesUseCase = mockk(relaxed = true)
    private val saveRecentSearch: SaveRecentSearchUseCase = mockk(relaxed = true)
    private val deleteRecentSearch: DeleteRecentSearchUseCase = mockk(relaxed = true)
    private val clearRecentSearches: ClearRecentSearchesUseCase = mockk(relaxed = true)

    @Before
    fun setup() {
        every { observeRecentSearches.invoke() } returns flowOf(emptyList())
        every { searchDeviceHistory.invoke(any()) } returns flowOf(emptyList())
    }

    private fun createViewModel(): SearchViewModel = SearchViewModel(
        searchDeviceHistory = searchDeviceHistory,
        observeRecentSearches = observeRecentSearches,
        saveRecentSearch = saveRecentSearch,
        deleteRecentSearch = deleteRecentSearch,
        clearRecentSearches = clearRecentSearches,
    )

    @Test
    fun initialState_hasEmptyQuery() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        assertEquals("", viewModel.uiState.value.query)
    }

    @Test
    fun deleteRecentSearch_callsDeleteUseCase() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        viewModel.deleteRecentSearch("Galaxy")
        advanceUntilIdle()

        coVerify { deleteRecentSearch.invoke("Galaxy") }
    }

    @Test
    fun clearAllRecentSearches_callsClearUseCase() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        viewModel.clearRecentSearches()
        advanceUntilIdle()

        coVerify { clearRecentSearches.invoke() }
    }

    @Test
    fun commitSearch_savesQueryToRecentSearches() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        viewModel.updateQuery("Pixel")
        viewModel.commitSearch()
        advanceUntilIdle()

        coVerify { saveRecentSearch.invoke("Pixel") }
    }
}
