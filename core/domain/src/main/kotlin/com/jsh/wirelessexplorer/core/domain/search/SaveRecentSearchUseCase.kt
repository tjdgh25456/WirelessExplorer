package com.jsh.wirelessexplorer.core.domain.search

interface SaveRecentSearchUseCase {
    suspend operator fun invoke(query: String)
}
