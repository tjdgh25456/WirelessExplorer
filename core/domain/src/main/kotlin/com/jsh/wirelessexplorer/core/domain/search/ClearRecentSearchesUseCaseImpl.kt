package com.jsh.wirelessexplorer.core.domain.search

import javax.inject.Inject

class ClearRecentSearchesUseCaseImpl @Inject constructor(
    private val repository: RecentSearchRepository,
) : ClearRecentSearchesUseCase {
    override suspend operator fun invoke() {
        repository.clearAll()
    }
}
