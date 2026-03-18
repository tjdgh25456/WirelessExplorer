package com.jsh.wirelessexplorer.core.data.di;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class BleDataModule_Companion_ProvideBluetoothAdapterFactory implements Factory<BluetoothAdapter> {
  private final Provider<Context> contextProvider;

  private BleDataModule_Companion_ProvideBluetoothAdapterFactory(
      Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public BluetoothAdapter get() {
    return provideBluetoothAdapter(contextProvider.get());
  }

  public static BleDataModule_Companion_ProvideBluetoothAdapterFactory create(
      Provider<Context> contextProvider) {
    return new BleDataModule_Companion_ProvideBluetoothAdapterFactory(contextProvider);
  }

  public static BluetoothAdapter provideBluetoothAdapter(Context context) {
    return Preconditions.checkNotNullFromProvides(BleDataModule.Companion.provideBluetoothAdapter(context));
  }
}
