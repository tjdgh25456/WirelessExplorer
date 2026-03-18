package com.jsh.wirelessexplorer.core.data.settings;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
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
public final class OnboardingRepositoryImpl_Factory implements Factory<OnboardingRepositoryImpl> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  private OnboardingRepositoryImpl_Factory(Provider<DataStore<Preferences>> dataStoreProvider) {
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public OnboardingRepositoryImpl get() {
    return newInstance(dataStoreProvider.get());
  }

  public static OnboardingRepositoryImpl_Factory create(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    return new OnboardingRepositoryImpl_Factory(dataStoreProvider);
  }

  public static OnboardingRepositoryImpl newInstance(DataStore<Preferences> dataStore) {
    return new OnboardingRepositoryImpl(dataStore);
  }
}
