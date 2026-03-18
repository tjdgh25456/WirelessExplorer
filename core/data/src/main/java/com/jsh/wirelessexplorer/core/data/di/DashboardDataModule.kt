package com.jsh.wirelessexplorer.core.data.di

import com.jsh.wirelessexplorer.core.data.dashboard.DashboardRepositoryImpl
import com.jsh.wirelessexplorer.core.domain.dashboard.DashboardRepository
import com.jsh.wirelessexplorer.core.domain.dashboard.ObserveDashboardStatsUseCase
import com.jsh.wirelessexplorer.core.domain.dashboard.ObserveDashboardStatsUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.dashboard.ObserveTrafficSpeedUseCase
import com.jsh.wirelessexplorer.core.domain.dashboard.ObserveTrafficSpeedUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.wifi.ObserveWifiStatusUseCase
import com.jsh.wirelessexplorer.core.domain.wifi.ObserveWifiStatusUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardDataModule {

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository

    @Binds
    abstract fun bindObserveDashboardStats(impl: ObserveDashboardStatsUseCaseImpl): ObserveDashboardStatsUseCase

    @Binds
    abstract fun bindObserveWifiStatus(impl: ObserveWifiStatusUseCaseImpl): ObserveWifiStatusUseCase

    @Binds
    abstract fun bindObserveTrafficSpeed(impl: ObserveTrafficSpeedUseCaseImpl): ObserveTrafficSpeedUseCase
}
