package com.jsh.wirelessexplorer.core.domain.search

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRecentSearchesUseCaseImpl @Inject constructor(
    private val repository: RecentSearchRepository,
) : ObserveRecentSearchesUseCase {
    override operator fun invoke(): Flow<List<String>> =
        repository.observeRecentSearches()
}
