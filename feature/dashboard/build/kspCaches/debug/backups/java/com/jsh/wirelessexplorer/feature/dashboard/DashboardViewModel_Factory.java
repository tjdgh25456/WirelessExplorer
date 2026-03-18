package com.jsh.wirelessexplorer.feature.dashboard;

import com.jsh.wirelessexplorer.core.domain.ble.ObserveBleConnectionsUseCase;
import com.jsh.wirelessexplorer.core.domain.dashboard.ObserveDashboardStatsUseCase;
import com.jsh.wirelessexplorer.core.domain.dashboard.ObserveTrafficSpeedUseCase;
import com.jsh.wirelessexplorer.core.domain.wifi.ObserveWifiStatusUseCase;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<ObserveWifiStatusUseCase> observeWifiStatusProvider;

  private final Provider<ObserveDashboardStatsUseCase> observeDashboardStatsProvider;

  private final Provider<ObserveBleConnectionsUseCase> observeBleConnectionsProvider;

  private final Provider<ObserveTrafficSpeedUseCase> observeTrafficSpeedProvider;

  private DashboardViewModel_Factory(Provider<ObserveWifiStatusUseCase> observeWifiStatusProvider,
      Provider<ObserveDashboardStatsUseCase> observeDashboardStatsProvider,
      Provider<ObserveBleConnectionsUseCase> observeBleConnectionsProvider,
      Provider<ObserveTrafficSpeedUseCase> observeTrafficSpeedProvider) {
    this.observeWifiStatusProvider = observeWifiStatusProvider;
    this.observeDashboardStatsProvider = observeDashboardStatsProvider;
    this.observeBleConnectionsProvider = observeBleConnectionsProvider;
    this.observeTrafficSpeedProvider = observeTrafficSpeedProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(observeWifiStatusProvider.get(), observeDashboardStatsProvider.get(), observeBleConnectionsProvider.get(), observeTrafficSpeedProvider.get());
  }

  public static DashboardViewModel_Factory create(
      Provider<ObserveWifiStatusUseCase> observeWifiStatusProvider,
      Provider<ObserveDashboardStatsUseCase> observeDashboardStatsProvider,
      Provider<ObserveBleConnectionsUseCase> observeBleConnectionsProvider,
      Provider<ObserveTrafficSpeedUseCase> observeTrafficSpeedProvider) {
    return new DashboardViewModel_Factory(observeWifiStatusProvider, observeDashboardStatsProvider, observeBleConnectionsProvider, observeTrafficSpeedProvider);
  }

  public static DashboardViewModel newInstance(ObserveWifiStatusUseCase observeWifiStatus,
      ObserveDashboardStatsUseCase observeDashboardStats,
      ObserveBleConnectionsUseCase observeBleConnections,
      ObserveTrafficSpeedUseCase observeTrafficSpeed) {
    return new DashboardViewModel(observeWifiStatus, observeDashboardStats, observeBleConnections, observeTrafficSpeed);
  }
}
