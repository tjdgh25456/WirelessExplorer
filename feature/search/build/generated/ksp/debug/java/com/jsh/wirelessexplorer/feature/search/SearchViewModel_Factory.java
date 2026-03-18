package com.jsh.wirelessexplorer.feature.search;

import com.jsh.wirelessexplorer.core.domain.history.SearchDeviceHistoryUseCase;
import com.jsh.wirelessexplorer.core.domain.search.ClearRecentSearchesUseCase;
import com.jsh.wirelessexplorer.core.domain.search.DeleteRecentSearchUseCase;
import com.jsh.wirelessexplorer.core.domain.search.ObserveRecentSearchesUseCase;
import com.jsh.wirelessexplorer.core.domain.search.SaveRecentSearchUseCase;
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
public final class SearchViewModel_Factory implements Factory<SearchViewModel> {
  private final Provider<SearchDeviceHistoryUseCase> searchDeviceHistoryProvider;

  private final Provider<ObserveRecentSearchesUseCase> observeRecentSearchesProvider;

  private final Provider<SaveRecentSearchUseCase> saveRecentSearchProvider;

  private final Provider<DeleteRecentSearchUseCase> deleteRecentSearchProvider;

  private final Provider<ClearRecentSearchesUseCase> clearRecentSearchesProvider;

  private SearchViewModel_Factory(Provider<SearchDeviceHistoryUseCase> searchDeviceHistoryProvider,
      Provider<ObserveRecentSearchesUseCase> observeRecentSearchesProvider,
      Provider<SaveRecentSearchUseCase> saveRecentSearchProvider,
      Provider<DeleteRecentSearchUseCase> deleteRecentSearchProvider,
      Provider<ClearRecentSearchesUseCase> clearRecentSearchesProvider) {
    this.searchDeviceHistoryProvider = searchDeviceHistoryProvider;
    this.observeRecentSearchesProvider = observeRecentSearchesProvider;
    this.saveRecentSearchProvider = saveRecentSearchProvider;
    this.deleteRecentSearchProvider = deleteRecentSearchProvider;
    this.clearRecentSearchesProvider = clearRecentSearchesProvider;
  }

  @Override
  public SearchViewModel get() {
    return newInstance(searchDeviceHistoryProvider.get(), observeRecentSearchesProvider.get(), saveRecentSearchProvider.get(), deleteRecentSearchProvider.get(), clearRecentSearchesProvider.get());
  }

  public static SearchViewModel_Factory create(
      Provider<SearchDeviceHistoryUseCase> searchDeviceHistoryProvider,
      Provider<ObserveRecentSearchesUseCase> observeRecentSearchesProvider,
      Provider<SaveRecentSearchUseCase> saveRecentSearchProvider,
      Provider<DeleteRecentSearchUseCase> deleteRecentSearchProvider,
      Provider<ClearRecentSearchesUseCase> clearRecentSearchesProvider) {
    return new SearchViewModel_Factory(searchDeviceHistoryProvider, observeRecentSearchesProvider, saveRecentSearchProvider, deleteRecentSearchProvider, clearRecentSearchesProvider);
  }

  public static SearchViewModel newInstance(SearchDeviceHistoryUseCase searchDeviceHistory,
      ObserveRecentSearchesUseCase observeRecentSearches, SaveRecentSearchUseCase saveRecentSearch,
      DeleteRecentSearchUseCase deleteRecentSearch,
      ClearRecentSearchesUseCase clearRecentSearches) {
    return new SearchViewModel(searchDeviceHistory, observeRecentSearches, saveRecentSearch, deleteRecentSearch, clearRecentSearches);
  }
}
