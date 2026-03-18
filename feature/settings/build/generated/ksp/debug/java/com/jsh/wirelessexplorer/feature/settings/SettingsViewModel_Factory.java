package com.jsh.wirelessexplorer.feature.settings;

import com.jsh.wirelessexplorer.core.domain.history.DeviceHistoryRepository;
import com.jsh.wirelessexplorer.core.domain.settings.ObserveSettingsUseCase;
import com.jsh.wirelessexplorer.core.domain.settings.SettingsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<ObserveSettingsUseCase> observeSettingsProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<DeviceHistoryRepository> historyRepositoryProvider;

  private SettingsViewModel_Factory(Provider<ObserveSettingsUseCase> observeSettingsProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<DeviceHistoryRepository> historyRepositoryProvider) {
    this.observeSettingsProvider = observeSettingsProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.historyRepositoryProvider = historyRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(observeSettingsProvider.get(), settingsRepositoryProvider.get(), historyRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<ObserveSettingsUseCase> observeSettingsProvider,
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<DeviceHistoryRepository> historyRepositoryProvider) {
    return new SettingsViewModel_Factory(observeSettingsProvider, settingsRepositoryProvider, historyRepositoryProvider);
  }

  public static SettingsViewModel newInstance(ObserveSettingsUseCase observeSettings,
      SettingsRepository settingsRepository, DeviceHistoryRepository historyRepository) {
    return new SettingsViewModel(observeSettings, settingsRepository, historyRepository);
  }
}
