package com.jsh.wirelessexplorer.core.data.ble;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import com.jsh.wirelessexplorer.core.common.TimeProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class BleScanner_Factory implements Factory<BleScanner> {
  private final Provider<Context> contextProvider;

  private final Provider<BluetoothAdapter> bluetoothAdapterProvider;

  private final Provider<TimeProvider> timeProvider;

  private BleScanner_Factory(Provider<Context> contextProvider,
      Provider<BluetoothAdapter> bluetoothAdapterProvider, Provider<TimeProvider> timeProvider) {
    this.contextProvider = contextProvider;
    this.bluetoothAdapterProvider = bluetoothAdapterProvider;
    this.timeProvider = timeProvider;
  }

  @Override
  public BleScanner get() {
    return newInstance(contextProvider.get(), bluetoothAdapterProvider.get(), timeProvider.get());
  }

  public static BleScanner_Factory create(Provider<Context> contextProvider,
      Provider<BluetoothAdapter> bluetoothAdapterProvider, Provider<TimeProvider> timeProvider) {
    return new BleScanner_Factory(contextProvider, bluetoothAdapterProvider, timeProvider);
  }

  public static BleScanner newInstance(Context context, BluetoothAdapter bluetoothAdapter,
      TimeProvider timeProvider) {
    return new BleScanner(context, bluetoothAdapter, timeProvider);
  }
}
