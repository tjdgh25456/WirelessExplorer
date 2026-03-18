package com.jsh.wirelessexplorer.core.data.di

import com.jsh.wirelessexplorer.core.data.uwb.UwbRepositoryImpl
import com.jsh.wirelessexplorer.core.domain.uwb.CheckUwbSupportUseCase
import com.jsh.wirelessexplorer.core.domain.uwb.CheckUwbSupportUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.uwb.StartUwbRangingUseCase
import com.jsh.wirelessexplorer.core.domain.uwb.StartUwbRangingUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.uwb.UwbRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UwbDataModule {

    @Binds
    @Singleton
    abstract fun bindUwbRepository(impl: UwbRepositoryImpl): UwbRepository

    @Binds
    abstract fun bindCheckUwbSupport(impl: CheckUwbSupportUseCaseImpl): CheckUwbSupportUseCase

    @Binds
    abstract fun bindStartUwbRanging(impl: StartUwbRangingUseCaseImpl): StartUwbRangingUseCase
}
