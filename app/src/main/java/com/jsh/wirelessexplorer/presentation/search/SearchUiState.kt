package com.jsh.wirelessexplorer.presentation.search

import com.jsh.wirelessexplorer.domain.model.DeviceHistory
import com.jsh.wirelessexplorer.domain.model.TechType

data class SearchUiState(
    val query: String = "",
    val results: List<DeviceHistory> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val selectedFilter: TechType? = null,
    val isSearching: Boolean = false,
    val isEmpty: Boolean = false,
)
