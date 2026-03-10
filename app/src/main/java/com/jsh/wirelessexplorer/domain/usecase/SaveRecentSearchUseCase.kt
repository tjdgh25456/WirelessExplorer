package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.data.datasource.RecentSearchDataSource
import javax.inject.Inject

class SaveRecentSearchUseCase @Inject constructor(
    private val dataSource: RecentSearchDataSource,
) {
    suspend operator fun invoke(query: String) {
        dataSource.saveSearch(query)
    }
}
