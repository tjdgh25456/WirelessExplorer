package com.jsh.wirelessexplorer.core.domain.search

interface DeleteRecentSearchUseCase {
    suspend operator fun invoke(query: String)
}
