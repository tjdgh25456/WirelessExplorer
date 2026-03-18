package com.jsh.wirelessexplorer.core.data.ble;

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
public final class BleRepositoryImpl_Factory implements Factory<BleRepositoryImpl> {
  private final Provider<BleScanner> scannerProvider;

  private final Provider<BleConnector> connectorProvider;

  private BleRepositoryImpl_Factory(Provider<BleScanner> scannerProvider,
      Provider<BleConnector> connectorProvider) {
    this.scannerProvider = scannerProvider;
    this.connectorProvider = connectorProvider;
  }

  @Override
  public BleRepositoryImpl get() {
    return newInstance(scannerProvider.get(), connectorProvider.get());
  }

  public static BleRepositoryImpl_Factory create(Provider<BleScanner> scannerProvider,
      Provider<BleConnector> connectorProvider) {
    return new BleRepositoryImpl_Factory(scannerProvider, connectorProvider);
  }

  public static BleRepositoryImpl newInstance(BleScanner scanner, BleConnector connector) {
    return new BleRepositoryImpl(scanner, connector);
  }
}
