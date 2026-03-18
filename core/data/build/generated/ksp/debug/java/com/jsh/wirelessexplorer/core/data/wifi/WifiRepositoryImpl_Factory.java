package com.jsh.wirelessexplorer.core.data.wifi;

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
public final class WifiRepositoryImpl_Factory implements Factory<WifiRepositoryImpl> {
  private final Provider<WifiScanner> scannerProvider;

  private WifiRepositoryImpl_Factory(Provider<WifiScanner> scannerProvider) {
    this.scannerProvider = scannerProvider;
  }

  @Override
  public WifiRepositoryImpl get() {
    return newInstance(scannerProvider.get());
  }

  public static WifiRepositoryImpl_Factory create(Provider<WifiScanner> scannerProvider) {
    return new WifiRepositoryImpl_Factory(scannerProvider);
  }

  public static WifiRepositoryImpl newInstance(WifiScanner scanner) {
    return new WifiRepositoryImpl(scanner);
  }
}
