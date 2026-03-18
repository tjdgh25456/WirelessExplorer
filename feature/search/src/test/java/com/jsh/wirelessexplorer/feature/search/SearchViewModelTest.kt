package com.jsh.wirelessexplorer.feature.search

import com.jsh.wirelessexplorer.core.domain.history.SearchDeviceHistoryUseCase
import com.jsh.wirelessexplorer.core.domain.search.ClearRecentSearchesUseCase
import com.jsh.wirelessexplorer.core.domain.search.DeleteRecentSearchUseCase
import com.jsh.wirelessexplorer.core.domain.search.ObserveRecentSearchesUseCase
import com.jsh.wirelessexplorer.core.domain.search.SaveRecentSearchUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

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

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
