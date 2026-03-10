package com.jsh.wirelessexplorer.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsh.wirelessexplorer.domain.model.TechType
import com.jsh.wirelessexplorer.domain.usecase.ClearRecentSearchesUseCase
import com.jsh.wirelessexplorer.domain.usecase.DeleteRecentSearchUseCase
import com.jsh.wirelessexplorer.domain.usecase.ObserveRecentSearchesUseCase
import com.jsh.wirelessexplorer.domain.usecase.SaveRecentSearchUseCase
import com.jsh.wirelessexplorer.domain.usecase.SearchDeviceHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchDeviceHistory: SearchDeviceHistoryUseCase,
    private val observeRecentSearches: ObserveRecentSearchesUseCase,
    private val saveRecentSearch: SaveRecentSearchUseCase,
    private val deleteRecentSearch: DeleteRecentSearchUseCase,
    private val clearRecentSearches: ClearRecentSearchesUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow<TechType?>(null)

    /** debounce 적용 후 검색 결과를 가져온다. */
    private val searchResults = _searchQuery
        .debounce(300L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) flowOf(emptyList())
            else searchDeviceHistory(query)
        }

    val uiState: StateFlow<SearchUiState> = combine(
        _searchQuery,
        searchResults,
        observeRecentSearches(),
        _selectedFilter,
    ) { query, results, recentSearches, filter ->
        // 기술 타입 필터 적용
        val filtered = if (filter != null) {
            results.filter { it.techType == filter }
        } else {
            results
        }

        SearchUiState(
            query = query,
            results = filtered,
            recentSearches = recentSearches,
            selectedFilter = filter,
            isSearching = query.isNotBlank(),
            isEmpty = query.isNotBlank() && filtered.isEmpty(),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SearchUiState(),
    )

    fun updateQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectFilter(techType: TechType?) {
        _selectedFilter.value = techType
    }

    /** 최근 검색어를 선택하여 검색어 입력란에 반영하고 저장한다. */
    fun selectRecentSearch(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            saveRecentSearch(query)
        }
    }

    /** 검색 실행 시 최근 검색어에 저장한다. */
    fun commitSearch() {
        val query = _searchQuery.value.trim()
        if (query.isBlank()) return
        viewModelScope.launch {
            saveRecentSearch(query)
            Timber.d("검색어 저장: $query")
        }
    }

    fun deleteRecentSearch(query: String) {
        viewModelScope.launch {
            deleteRecentSearch.invoke(query)
        }
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            clearRecentSearches.invoke()
            Timber.d("최근 검색어 전체 삭제")
        }
    }
}
