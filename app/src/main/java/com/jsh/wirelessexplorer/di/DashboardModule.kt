package com.jsh.wirelessexplorer.di

import com.jsh.wirelessexplorer.data.repository.DashboardRepositoryImpl
import com.jsh.wirelessexplorer.domain.repository.DashboardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardModule {

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        impl: DashboardRepositoryImpl,
    ): DashboardRepository
}
