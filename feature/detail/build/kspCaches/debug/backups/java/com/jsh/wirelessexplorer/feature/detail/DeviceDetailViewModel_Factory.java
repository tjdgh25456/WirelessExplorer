package com.jsh.wirelessexplorer.feature.detail;

import androidx.lifecycle.SavedStateHandle;
import com.jsh.wirelessexplorer.core.domain.history.DeleteDeviceHistoryUseCase;
import com.jsh.wirelessexplorer.core.domain.history.ObserveDeviceDetailUseCase;
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
public final class DeviceDetailViewModel_Factory implements Factory<DeviceDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<ObserveDeviceDetailUseCase> observeDeviceDetailProvider;

  private final Provider<DeleteDeviceHistoryUseCase> deleteDeviceHistoryProvider;

  private final Provider<ToggleFavoriteUseCase> toggleFavoriteProvider;

  private DeviceDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<ObserveDeviceDetailUseCase> observeDeviceDetailProvider,
      Provider<DeleteDeviceHistoryUseCase> deleteDeviceHistoryProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.observeDeviceDetailProvider = observeDeviceDetailProvider;
    this.deleteDeviceHistoryProvider = deleteDeviceHistoryProvider;
    this.toggleFavoriteProvider = toggleFavoriteProvider;
  }

  @Override
  public DeviceDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), observeDeviceDetailProvider.get(), deleteDeviceHistoryProvider.get(), toggleFavoriteProvider.get());
  }

  public static DeviceDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<ObserveDeviceDetailUseCase> observeDeviceDetailProvider,
      Provider<DeleteDeviceHistoryUseCase> deleteDeviceHistoryProvider,
      Provider<ToggleFavoriteUseCase> toggleFavoriteProvider) {
    return new DeviceDetailViewModel_Factory(savedStateHandleProvider, observeDeviceDetailProvider, deleteDeviceHistoryProvider, toggleFavoriteProvider);
  }

  public static DeviceDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      ObserveDeviceDetailUseCase observeDeviceDetail,
      DeleteDeviceHistoryUseCase deleteDeviceHistory, ToggleFavoriteUseCase toggleFavorite) {
    return new DeviceDetailViewModel(savedStateHandle, observeDeviceDetail, deleteDeviceHistory, toggleFavorite);
  }
}
