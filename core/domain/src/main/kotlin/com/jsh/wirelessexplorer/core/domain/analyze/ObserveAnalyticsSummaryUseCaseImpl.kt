package com.jsh.wirelessexplorer.core.domain.analyze

import com.jsh.wirelessexplorer.core.model.AnalyticsSummary
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAnalyticsSummaryUseCaseImpl @Inject constructor(
    private val repository: AnalyzeRepository,
) : ObserveAnalyticsSummaryUseCase {
    override operator fun invoke(): Flow<AnalyticsSummary> = repository.observeSummary()
}
