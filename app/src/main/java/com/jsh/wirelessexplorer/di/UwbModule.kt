package com.jsh.wirelessexplorer.di

import com.jsh.wirelessexplorer.data.repository.UwbRepositoryImpl
import com.jsh.wirelessexplorer.domain.repository.UwbRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UwbModule {

    @Binds
    @Singleton
    abstract fun bindUwbRepository(impl: UwbRepositoryImpl): UwbRepository
}
