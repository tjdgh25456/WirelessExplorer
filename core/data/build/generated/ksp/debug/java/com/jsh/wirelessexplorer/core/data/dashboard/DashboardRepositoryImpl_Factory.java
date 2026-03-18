package com.jsh.wirelessexplorer.core.data.dashboard;

import com.jsh.wirelessexplorer.core.domain.history.DeviceHistoryRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineDispatcher;

@ScopeMetadata
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
public final class DashboardRepositoryImpl_Factory implements Factory<DashboardRepositoryImpl> {
  private final Provider<WifiStatusProvider> wifiStatusProvider;

  private final Provider<TrafficStatsProvider> trafficStatsProvider;

  private final Provider<DeviceHistoryRepository> deviceHistoryRepositoryProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  private DashboardRepositoryImpl_Factory(Provider<WifiStatusProvider> wifiStatusProvider,
      Provider<TrafficStatsProvider> trafficStatsProvider,
      Provider<DeviceHistoryRepository> deviceHistoryRepositoryProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.wifiStatusProvider = wifiStatusProvider;
    this.trafficStatsProvider = trafficStatsProvider;
    this.deviceHistoryRepositoryProvider = deviceHistoryRepositoryProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public DashboardRepositoryImpl get() {
    return newInstance(wifiStatusProvider.get(), trafficStatsProvider.get(), deviceHistoryRepositoryProvider.get(), ioDispatcherProvider.get());
  }

  public static DashboardRepositoryImpl_Factory create(
      Provider<WifiStatusProvider> wifiStatusProvider,
      Provider<TrafficStatsProvider> trafficStatsProvider,
      Provider<DeviceHistoryRepository> deviceHistoryRepositoryProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new DashboardRepositoryImpl_Factory(wifiStatusProvider, trafficStatsProvider, deviceHistoryRepositoryProvider, ioDispatcherProvider);
  }

  public static DashboardRepositoryImpl newInstance(WifiStatusProvider wifiStatusProvider,
      TrafficStatsProvider trafficStatsProvider, DeviceHistoryRepository deviceHistoryRepository,
      CoroutineDispatcher ioDispatcher) {
    return new DashboardRepositoryImpl(wifiStatusProvider, trafficStatsProvider, deviceHistoryRepository, ioDispatcher);
  }
}
