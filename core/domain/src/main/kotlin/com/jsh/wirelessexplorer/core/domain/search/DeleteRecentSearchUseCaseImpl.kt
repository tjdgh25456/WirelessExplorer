package com.jsh.wirelessexplorer.core.domain.search

import javax.inject.Inject

class DeleteRecentSearchUseCaseImpl @Inject constructor(
    private val repository: RecentSearchRepository,
) : DeleteRecentSearchUseCase {
    override suspend operator fun invoke(query: String) {
        repository.deleteSearch(query)
    }
}
