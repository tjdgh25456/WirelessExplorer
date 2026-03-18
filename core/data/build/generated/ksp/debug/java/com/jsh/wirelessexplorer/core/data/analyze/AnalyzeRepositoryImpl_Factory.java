package com.jsh.wirelessexplorer.core.data.analyze;

import com.jsh.wirelessexplorer.core.domain.history.DeviceHistoryRepository;
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
public final class AnalyzeRepositoryImpl_Factory implements Factory<AnalyzeRepositoryImpl> {
  private final Provider<DeviceHistoryRepository> historyRepositoryProvider;

  private AnalyzeRepositoryImpl_Factory(
      Provider<DeviceHistoryRepository> historyRepositoryProvider) {
    this.historyRepositoryProvider = historyRepositoryProvider;
  }

  @Override
  public AnalyzeRepositoryImpl get() {
    return newInstance(historyRepositoryProvider.get());
  }

  public static AnalyzeRepositoryImpl_Factory create(
      Provider<DeviceHistoryRepository> historyRepositoryProvider) {
    return new AnalyzeRepositoryImpl_Factory(historyRepositoryProvider);
  }

  public static AnalyzeRepositoryImpl newInstance(DeviceHistoryRepository historyRepository) {
    return new AnalyzeRepositoryImpl(historyRepository);
  }
}
