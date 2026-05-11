package com.jsh.wirelessexplorer.core.data.di

import com.jsh.wirelessexplorer.core.data.history.DeviceHistoryRepositoryImpl
import com.jsh.wirelessexplorer.core.domain.history.DeleteAllHistoryUseCase
import com.jsh.wirelessexplorer.core.domain.history.DeleteAllHistoryUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.history.DeleteDeviceHistoryUseCase
import com.jsh.wirelessexplorer.core.domain.history.DeleteDeviceHistoryUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.history.DeviceHistoryRepository
import com.jsh.wirelessexplorer.core.domain.history.GetOrCreateDeviceHistoryIdUseCase
import com.jsh.wirelessexplorer.core.domain.history.GetOrCreateDeviceHistoryIdUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.history.ObserveDeviceDetailUseCase
import com.jsh.wirelessexplorer.core.domain.history.ObserveDeviceDetailUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.history.ObserveDeviceHistoryUseCase
import com.jsh.wirelessexplorer.core.domain.history.ObserveDeviceHistoryUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.history.RestoreDeviceHistoryUseCase
import com.jsh.wirelessexplorer.core.domain.history.RestoreDeviceHistoryUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.history.SearchDeviceHistoryUseCase
import com.jsh.wirelessexplorer.core.domain.history.SearchDeviceHistoryUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.history.ToggleFavoriteUseCase
import com.jsh.wirelessexplorer.core.domain.history.ToggleFavoriteUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HistoryDataModule {

    @Binds
    @Singleton
    abstract fun bindDeviceHistoryRepository(impl: DeviceHistoryRepositoryImpl): DeviceHistoryRepository

    @Binds
    abstract fun bindObserveDeviceHistory(impl: ObserveDeviceHistoryUseCaseImpl): ObserveDeviceHistoryUseCase

    @Binds
    abstract fun bindObserveDeviceDetail(impl: ObserveDeviceDetailUseCaseImpl): ObserveDeviceDetailUseCase

    @Binds
    abstract fun bindSearchDeviceHistory(impl: SearchDeviceHistoryUseCaseImpl): SearchDeviceHistoryUseCase

    @Binds
    abstract fun bindDeleteDeviceHistory(impl: DeleteDeviceHistoryUseCaseImpl): DeleteDeviceHistoryUseCase

    @Binds
    abstract fun bindDeleteAllHistory(impl: DeleteAllHistoryUseCaseImpl): DeleteAllHistoryUseCase

    @Binds
    abstract fun bindRestoreDeviceHistory(impl: RestoreDeviceHistoryUseCaseImpl): RestoreDeviceHistoryUseCase

    @Binds
    abstract fun bindToggleFavorite(impl: ToggleFavoriteUseCaseImpl): ToggleFavoriteUseCase

    @Binds
    abstract fun bindGetOrCreateDeviceHistoryId(impl: GetOrCreateDeviceHistoryIdUseCaseImpl): GetOrCreateDeviceHistoryIdUseCase
}
