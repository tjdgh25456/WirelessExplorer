package com.jsh.wirelessexplorer.core.domain.analyze

import com.jsh.wirelessexplorer.core.model.TechDistribution
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTechDistributionUseCaseImpl @Inject constructor(
    private val repository: AnalyzeRepository,
) : ObserveTechDistributionUseCase {
    override operator fun invoke(): Flow<List<TechDistribution>> = repository.observeTechDistribution()
}
