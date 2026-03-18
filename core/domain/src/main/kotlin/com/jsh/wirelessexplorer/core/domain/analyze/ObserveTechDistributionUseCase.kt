package com.jsh.wirelessexplorer.core.domain.analyze

import com.jsh.wirelessexplorer.core.model.TechDistribution
import kotlinx.coroutines.flow.Flow

interface ObserveTechDistributionUseCase {
    operator fun invoke(): Flow<List<TechDistribution>>
}
