package com.jsh.wirelessexplorer.core.domain.search

import kotlinx.coroutines.flow.Flow

interface ObserveRecentSearchesUseCase {
    operator fun invoke(): Flow<List<String>>
}
