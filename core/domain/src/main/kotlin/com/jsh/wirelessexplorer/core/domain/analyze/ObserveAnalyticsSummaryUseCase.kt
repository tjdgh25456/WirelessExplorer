package com.jsh.wirelessexplorer.core.domain.analyze

import com.jsh.wirelessexplorer.core.model.AnalyticsSummary
import kotlinx.coroutines.flow.Flow

interface ObserveAnalyticsSummaryUseCase {
    operator fun invoke(): Flow<AnalyticsSummary>
}
