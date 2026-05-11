package com.jsh.wirelessexplorer.di

import com.jsh.wirelessexplorer.core.common.SystemTimeProvider
import com.jsh.wirelessexplorer.core.common.TimeProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TimeProviderModule {

    @Binds
    abstract fun bindTimeProvider(impl: SystemTimeProvider): TimeProvider
}
