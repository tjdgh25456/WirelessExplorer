package com.jsh.wirelessexplorer.core.data.search

import com.jsh.wirelessexplorer.core.domain.search.RecentSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecentSearchRepositoryImpl @Inject constructor(
    private val dataSource: RecentSearchDataSource,
) : RecentSearchRepository {

    override fun observeRecentSearches(): Flow<List<String>> =
        dataSource.observeRecentSearches()

    override suspend fun saveSearch(query: String) {
        dataSource.saveSearch(query)
    }

    override suspend fun deleteSearch(query: String) {
        dataSource.deleteSearch(query)
    }

    override suspend fun clearAll() {
        dataSource.clearAll()
    }
}
