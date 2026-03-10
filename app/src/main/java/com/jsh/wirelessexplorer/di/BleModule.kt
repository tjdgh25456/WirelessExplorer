package com.jsh.wirelessexplorer.di

import com.jsh.wirelessexplorer.data.repository.BleRepositoryImpl
import com.jsh.wirelessexplorer.domain.repository.BleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BleModule {

    @Binds
    @Singleton
    abstract fun bindBleRepository(impl: BleRepositoryImpl): BleRepository
}
