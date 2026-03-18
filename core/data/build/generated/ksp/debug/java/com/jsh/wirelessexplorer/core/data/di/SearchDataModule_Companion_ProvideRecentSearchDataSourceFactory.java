package com.jsh.wirelessexplorer.core.data.di;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import com.jsh.wirelessexplorer.core.data.search.RecentSearchDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class SearchDataModule_Companion_ProvideRecentSearchDataSourceFactory implements Factory<RecentSearchDataSource> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  private SearchDataModule_Companion_ProvideRecentSearchDataSourceFactory(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public RecentSearchDataSource get() {
    return provideRecentSearchDataSource(dataStoreProvider.get());
  }

  public static SearchDataModule_Companion_ProvideRecentSearchDataSourceFactory create(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    return new SearchDataModule_Companion_ProvideRecentSearchDataSourceFactory(dataStoreProvider);
  }

  public static RecentSearchDataSource provideRecentSearchDataSource(
      DataStore<Preferences> dataStore) {
    return Preconditions.checkNotNullFromProvides(SearchDataModule.Companion.provideRecentSearchDataSource(dataStore));
  }
}
