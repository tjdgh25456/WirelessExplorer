package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.data.datasource.RecentSearchDataSource
import javax.inject.Inject

class DeleteRecentSearchUseCase @Inject constructor(
    private val dataSource: RecentSearchDataSource,
) {
    suspend operator fun invoke(query: String) {
        dataSource.deleteSearch(query)
    }
}
