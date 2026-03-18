package com.jsh.wirelessexplorer.core.domain.search

import javax.inject.Inject

class SaveRecentSearchUseCaseImpl @Inject constructor(
    private val repository: RecentSearchRepository,
) : SaveRecentSearchUseCase {
    override suspend operator fun invoke(query: String) {
        repository.saveSearch(query)
    }
}
