package com.jsh.wirelessexplorer.core.data.settings;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineDispatcher;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("com.jsh.wirelessexplorer.core.common.IoDispatcher")
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
public final class SettingsDataSource_Factory implements Factory<SettingsDataSource> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  private SettingsDataSource_Factory(Provider<DataStore<Preferences>> dataStoreProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.dataStoreProvider = dataStoreProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public SettingsDataSource get() {
    return newInstance(dataStoreProvider.get(), ioDispatcherProvider.get());
  }

  public static SettingsDataSource_Factory create(
      Provider<DataStore<Preferences>> dataStoreProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new SettingsDataSource_Factory(dataStoreProvider, ioDispatcherProvider);
  }

  public static SettingsDataSource newInstance(DataStore<Preferences> dataStore,
      CoroutineDispatcher ioDispatcher) {
    return new SettingsDataSource(dataStore, ioDispatcher);
  }
}
