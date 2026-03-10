package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.TechDistribution
import com.jsh.wirelessexplorer.domain.repository.AnalyzeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTechDistributionUseCase @Inject constructor(
    private val repository: AnalyzeRepository,
) {
    operator fun invoke(): Flow<List<TechDistribution>> = repository.observeTechDistribution()
}
