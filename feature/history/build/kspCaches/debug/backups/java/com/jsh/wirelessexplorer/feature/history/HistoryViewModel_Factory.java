package com.jsh.wirelessexplorer.feature.history;

import com.jsh.wirelessexplorer.core.domain.history.DeleteAllHistoryUseCase;
import com.jsh.wirelessexplorer.core.domain.history.DeleteDeviceHistoryUseCase;
import com.jsh.wirelessexplorer.core.domain.history.ObserveDeviceHistoryUseCase;
import com.jsh.wirelessexplorer.core.domain.history.RestoreDeviceHistoryUseCase;
import com.jsh.wirelessexplorer.core.domain.history.ToggleFavoriteUseCase;
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
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<ObserveDeviceHistoryUseCase> observeDeviceHistoryProvider;

  private final Provider<ToggleFavoriteUseCase> toggleFavoriteProvider;

  private final Provider<DeleteDeviceHistoryUseCase> deleteDeviceHistoryProvider;

  private final Provider<DeleteAllHistoryUseCase> deleteAllHistoryProvider;

  private final Provider<RestoreDeviceHistoryUseCase> restoreDeviceHistoryProvider;

  private HistoryViewModel_Factory(
      Provider<ObserveDeviceHistoryUseCase> observeDeviceHistoryProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteProvider,
      Provider<DeleteDeviceHistoryUseCase> deleteDeviceHistoryProvider,
      Provider<DeleteAllHistoryUseCase> deleteAllHistoryProvider,
      Provider<RestoreDeviceHistoryUseCase> restoreDeviceHistoryProvider) {
    this.observeDeviceHistoryProvider = observeDeviceHistoryProvider;
    this.toggleFavoriteProvider = toggleFavoriteProvider;
    this.deleteDeviceHistoryProvider = deleteDeviceHistoryProvider;
    this.deleteAllHistoryProvider = deleteAllHistoryProvider;
    this.restoreDeviceHistoryProvider = restoreDeviceHistoryProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(observeDeviceHistoryProvider.get(), toggleFavoriteProvider.get(), deleteDeviceHistoryProvider.get(), deleteAllHistoryProvider.get(), restoreDeviceHistoryProvider.get());
  }

  public static HistoryViewModel_Factory create(
      Provider<ObserveDeviceHistoryUseCase> observeDeviceHistoryProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteProvider,
      Provider<DeleteDeviceHistoryUseCase> deleteDeviceHistoryProvider,
      Provider<DeleteAllHistoryUseCase> deleteAllHistoryProvider,
      Provider<RestoreDeviceHistoryUseCase> restoreDeviceHistoryProvider) {
    return new HistoryViewModel_Factory(observeDeviceHistoryProvider, toggleFavoriteProvider, deleteDeviceHistoryProvider, deleteAllHistoryProvider, restoreDeviceHistoryProvider);
  }

  public static HistoryViewModel newInstance(ObserveDeviceHistoryUseCase observeDeviceHistory,
      ToggleFavoriteUseCase toggleFavorite, DeleteDeviceHistoryUseCase deleteDeviceHistory,
      DeleteAllHistoryUseCase deleteAllHistory, RestoreDeviceHistoryUseCase restoreDeviceHistory) {
    return new HistoryViewModel(observeDeviceHistory, toggleFavorite, deleteDeviceHistory, deleteAllHistory, restoreDeviceHistory);
  }
}
