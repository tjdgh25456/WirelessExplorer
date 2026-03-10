package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.data.datasource.RecentSearchDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRecentSearchesUseCase @Inject constructor(
    private val dataSource: RecentSearchDataSource,
) {
    operator fun invoke(): Flow<List<String>> =
        dataSource.observeRecentSearches()
}
