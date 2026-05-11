package com.jsh.wirelessexplorer.core.domain.search

import kotlinx.coroutines.flow.Flow

interface RecentSearchRepository {
    fun observeRecentSearches(): Flow<List<String>>
    suspend fun saveSearch(query: String)
    suspend fun deleteSearch(query: String)
    suspend fun clearAll()
}
