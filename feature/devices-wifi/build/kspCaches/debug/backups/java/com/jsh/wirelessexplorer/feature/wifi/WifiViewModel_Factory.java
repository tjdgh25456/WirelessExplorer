package com.jsh.wirelessexplorer.feature.wifi;

import com.jsh.wirelessexplorer.core.domain.dashboard.ObserveTrafficSpeedUseCase;
import com.jsh.wirelessexplorer.core.domain.wifi.ObserveWifiConnectionUseCase;
import com.jsh.wirelessexplorer.core.domain.wifi.ScanWifiNetworksUseCase;
import com.jsh.wirelessexplorer.core.domain.wifi.WifiRepository;
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
public final class WifiViewModel_Factory implements Factory<WifiViewModel> {
  private final Provider<ScanWifiNetworksUseCase> scanWifiNetworksProvider;

  private final Provider<ObserveWifiConnectionUseCase> observeWifiConnectionProvider;

  private final Provider<ObserveTrafficSpeedUseCase> observeTrafficSpeedProvider;

  private final Provider<WifiRepository> wifiRepositoryProvider;

  private WifiViewModel_Factory(Provider<ScanWifiNetworksUseCase> scanWifiNetworksProvider,
      Provider<ObserveWifiConnectionUseCase> observeWifiConnectionProvider,
      Provider<ObserveTrafficSpeedUseCase> observeTrafficSpeedProvider,
      Provider<WifiRepository> wifiRepositoryProvider) {
    this.scanWifiNetworksProvider = scanWifiNetworksProvider;
    this.observeWifiConnectionProvider = observeWifiConnectionProvider;
    this.observeTrafficSpeedProvider = observeTrafficSpeedProvider;
    this.wifiRepositoryProvider = wifiRepositoryProvider;
  }

  @Override
  public WifiViewModel get() {
    return newInstance(scanWifiNetworksProvider.get(), observeWifiConnectionProvider.get(), observeTrafficSpeedProvider.get(), wifiRepositoryProvider.get());
  }

  public static WifiViewModel_Factory create(
      Provider<ScanWifiNetworksUseCase> scanWifiNetworksProvider,
      Provider<ObserveWifiConnectionUseCase> observeWifiConnectionProvider,
      Provider<ObserveTrafficSpeedUseCase> observeTrafficSpeedProvider,
      Provider<WifiRepository> wifiRepositoryProvider) {
    return new WifiViewModel_Factory(scanWifiNetworksProvider, observeWifiConnectionProvider, observeTrafficSpeedProvider, wifiRepositoryProvider);
  }

  public static WifiViewModel newInstance(ScanWifiNetworksUseCase scanWifiNetworks,
      ObserveWifiConnectionUseCase observeWifiConnection,
      ObserveTrafficSpeedUseCase observeTrafficSpeed, WifiRepository wifiRepository) {
    return new WifiViewModel(scanWifiNetworks, observeWifiConnection, observeTrafficSpeed, wifiRepository);
  }
}
