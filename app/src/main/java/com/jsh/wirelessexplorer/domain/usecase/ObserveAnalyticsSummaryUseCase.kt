package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.AnalyticsSummary
import com.jsh.wirelessexplorer.domain.repository.AnalyzeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAnalyticsSummaryUseCase @Inject constructor(
    private val repository: AnalyzeRepository,
) {
    operator fun invoke(): Flow<AnalyticsSummary> = repository.observeSummary()
}
