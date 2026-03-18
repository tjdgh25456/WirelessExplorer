package com.jsh.wirelessexplorer.core.data.di

import com.jsh.wirelessexplorer.core.data.wifi.WifiRepositoryImpl
import com.jsh.wirelessexplorer.core.domain.wifi.GetWifiEnabledUseCase
import com.jsh.wirelessexplorer.core.domain.wifi.GetWifiEnabledUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.wifi.ObserveWifiConnectionUseCase
import com.jsh.wirelessexplorer.core.domain.wifi.ObserveWifiConnectionUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.wifi.ObserveWifiEnabledUseCase
import com.jsh.wirelessexplorer.core.domain.wifi.ObserveWifiEnabledUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.wifi.ScanWifiNetworksUseCase
import com.jsh.wirelessexplorer.core.domain.wifi.ScanWifiNetworksUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.wifi.WifiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WifiDataModule {

    @Binds
    @Singleton
    abstract fun bindWifiRepository(impl: WifiRepositoryImpl): WifiRepository

    @Binds
    abstract fun bindScanWifiNetworks(impl: ScanWifiNetworksUseCaseImpl): ScanWifiNetworksUseCase

    @Binds
    abstract fun bindObserveWifiConnection(impl: ObserveWifiConnectionUseCaseImpl): ObserveWifiConnectionUseCase

    @Binds
    abstract fun bindGetWifiEnabled(impl: GetWifiEnabledUseCaseImpl): GetWifiEnabledUseCase

    @Binds
    abstract fun bindObserveWifiEnabled(impl: ObserveWifiEnabledUseCaseImpl): ObserveWifiEnabledUseCase
}
