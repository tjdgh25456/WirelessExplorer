package com.jsh.wirelessexplorer.core.data.ble;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineDispatcher;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata({
    "dagger.hilt.android.qualifiers.ApplicationContext",
    "com.jsh.wirelessexplorer.core.common.IoDispatcher"
})
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
public final class BleConnector_Factory implements Factory<BleConnector> {
  private final Provider<Context> contextProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  private final Provider<BluetoothAdapter> bluetoothAdapterProvider;

  private BleConnector_Factory(Provider<Context> contextProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider,
      Provider<BluetoothAdapter> bluetoothAdapterProvider) {
    this.contextProvider = contextProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
    this.bluetoothAdapterProvider = bluetoothAdapterProvider;
  }

  @Override
  public BleConnector get() {
    return newInstance(contextProvider.get(), ioDispatcherProvider.get(), bluetoothAdapterProvider.get());
  }

  public static BleConnector_Factory create(Provider<Context> contextProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider,
      Provider<BluetoothAdapter> bluetoothAdapterProvider) {
    return new BleConnector_Factory(contextProvider, ioDispatcherProvider, bluetoothAdapterProvider);
  }

  public static BleConnector newInstance(Context context, CoroutineDispatcher ioDispatcher,
      BluetoothAdapter bluetoothAdapter) {
    return new BleConnector(context, ioDispatcher, bluetoothAdapter);
  }
}
