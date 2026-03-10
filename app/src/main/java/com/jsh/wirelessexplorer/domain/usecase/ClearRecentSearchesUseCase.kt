package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.data.datasource.RecentSearchDataSource
import javax.inject.Inject

class ClearRecentSearchesUseCase @Inject constructor(
    private val dataSource: RecentSearchDataSource,
) {
    suspend operator fun invoke() {
        dataSource.clearAll()
    }
}
