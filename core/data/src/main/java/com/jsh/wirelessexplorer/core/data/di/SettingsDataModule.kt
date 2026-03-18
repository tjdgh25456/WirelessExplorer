package com.jsh.wirelessexplorer.core.data.di

import com.jsh.wirelessexplorer.core.data.settings.OnboardingRepositoryImpl
import com.jsh.wirelessexplorer.core.data.settings.SettingsRepositoryImpl
import com.jsh.wirelessexplorer.core.domain.settings.CheckOnboardingUseCase
import com.jsh.wirelessexplorer.core.domain.settings.CheckOnboardingUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.settings.CompleteOnboardingUseCase
import com.jsh.wirelessexplorer.core.domain.settings.CompleteOnboardingUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.settings.ObserveSettingsUseCase
import com.jsh.wirelessexplorer.core.domain.settings.ObserveSettingsUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.settings.OnboardingRepository
import com.jsh.wirelessexplorer.core.domain.settings.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsDataModule {

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(impl: OnboardingRepositoryImpl): OnboardingRepository

    @Binds
    abstract fun bindObserveSettings(impl: ObserveSettingsUseCaseImpl): ObserveSettingsUseCase

    @Binds
    abstract fun bindCheckOnboarding(impl: CheckOnboardingUseCaseImpl): CheckOnboardingUseCase

    @Binds
    abstract fun bindCompleteOnboarding(impl: CompleteOnboardingUseCaseImpl): CompleteOnboardingUseCase
}
