package com.jsh.wirelessexplorer.core.data.di

import com.jsh.wirelessexplorer.core.data.analyze.AnalyzeRepositoryImpl
import com.jsh.wirelessexplorer.core.domain.analyze.AnalyzeRepository
import com.jsh.wirelessexplorer.core.domain.analyze.ObserveAnalyticsSummaryUseCase
import com.jsh.wirelessexplorer.core.domain.analyze.ObserveAnalyticsSummaryUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.analyze.ObserveTechDistributionUseCase
import com.jsh.wirelessexplorer.core.domain.analyze.ObserveTechDistributionUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.analyze.ObserveTopDevicesUseCase
import com.jsh.wirelessexplorer.core.domain.analyze.ObserveTopDevicesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyzeDataModule {

    @Binds
    @Singleton
    abstract fun bindAnalyzeRepository(impl: AnalyzeRepositoryImpl): AnalyzeRepository

    @Binds
    abstract fun bindObserveAnalyticsSummary(impl: ObserveAnalyticsSummaryUseCaseImpl): ObserveAnalyticsSummaryUseCase

    @Binds
    abstract fun bindObserveTechDistribution(impl: ObserveTechDistributionUseCaseImpl): ObserveTechDistributionUseCase

    @Binds
    abstract fun bindObserveTopDevices(impl: ObserveTopDevicesUseCaseImpl): ObserveTopDevicesUseCase
}
