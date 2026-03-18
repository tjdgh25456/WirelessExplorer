package com.jsh.wirelessexplorer.core.data.settings;

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
public final class SettingsRepositoryImpl_Factory implements Factory<SettingsRepositoryImpl> {
  private final Provider<SettingsDataSource> dataSourceProvider;

  private SettingsRepositoryImpl_Factory(Provider<SettingsDataSource> dataSourceProvider) {
    this.dataSourceProvider = dataSourceProvider;
  }

  @Override
  public SettingsRepositoryImpl get() {
    return newInstance(dataSourceProvider.get());
  }

  public static SettingsRepositoryImpl_Factory create(
      Provider<SettingsDataSource> dataSourceProvider) {
    return new SettingsRepositoryImpl_Factory(dataSourceProvider);
  }

  public static SettingsRepositoryImpl newInstance(SettingsDataSource dataSource) {
    return new SettingsRepositoryImpl(dataSource);
  }
}
