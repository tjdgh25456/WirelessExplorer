package com.jsh.wirelessexplorer.di

import com.jsh.wirelessexplorer.data.repository.WifiRepositoryImpl
import com.jsh.wirelessexplorer.domain.repository.WifiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WifiModule {

    @Binds
    @Singleton
    abstract fun bindWifiRepository(impl: WifiRepositoryImpl): WifiRepository
}
