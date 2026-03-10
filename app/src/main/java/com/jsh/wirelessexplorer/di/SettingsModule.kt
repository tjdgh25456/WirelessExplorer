package com.jsh.wirelessexplorer.di

import com.jsh.wirelessexplorer.data.repository.AnalyzeRepositoryImpl
import com.jsh.wirelessexplorer.data.repository.OnboardingRepositoryImpl
import com.jsh.wirelessexplorer.data.repository.SettingsRepositoryImpl
import com.jsh.wirelessexplorer.domain.repository.AnalyzeRepository
import com.jsh.wirelessexplorer.domain.repository.OnboardingRepository
import com.jsh.wirelessexplorer.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository

    @Binds
    @Singleton
    abstract fun bindAnalyzeRepository(impl: AnalyzeRepositoryImpl): AnalyzeRepository
}
