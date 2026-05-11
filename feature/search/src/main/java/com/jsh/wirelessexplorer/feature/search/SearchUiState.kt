package com.jsh.wirelessexplorer.feature.search

import com.jsh.wirelessexplorer.core.model.DeviceHistory
import com.jsh.wirelessexplorer.core.model.TechType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SearchUiState(
    val query: String = "",
    val results: ImmutableList<DeviceHistory> = persistentListOf(),
    val recentSearches: ImmutableList<String> = persistentListOf(),
    val selectedFilter: TechType? = null,
    val isSearching: Boolean = false,
    val isEmpty: Boolean = false,
)
