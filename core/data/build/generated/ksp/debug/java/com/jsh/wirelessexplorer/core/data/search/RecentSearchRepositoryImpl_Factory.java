package com.jsh.wirelessexplorer.core.data.search;

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
public final class RecentSearchRepositoryImpl_Factory implements Factory<RecentSearchRepositoryImpl> {
  private final Provider<RecentSearchDataSource> dataSourceProvider;

  private RecentSearchRepositoryImpl_Factory(Provider<RecentSearchDataSource> dataSourceProvider) {
    this.dataSourceProvider = dataSourceProvider;
  }

  @Override
  public RecentSearchRepositoryImpl get() {
    return newInstance(dataSourceProvider.get());
  }

  public static RecentSearchRepositoryImpl_Factory create(
      Provider<RecentSearchDataSource> dataSourceProvider) {
    return new RecentSearchRepositoryImpl_Factory(dataSourceProvider);
  }

  public static RecentSearchRepositoryImpl newInstance(RecentSearchDataSource dataSource) {
    return new RecentSearchRepositoryImpl(dataSource);
  }
}
