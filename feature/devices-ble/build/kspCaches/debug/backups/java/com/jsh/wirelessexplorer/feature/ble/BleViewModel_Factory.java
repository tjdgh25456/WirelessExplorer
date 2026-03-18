package com.jsh.wirelessexplorer.feature.ble;

import com.jsh.wirelessexplorer.core.domain.ble.ConnectBleDeviceUseCase;
import com.jsh.wirelessexplorer.core.domain.ble.DisconnectBleDeviceUseCase;
import com.jsh.wirelessexplorer.core.domain.ble.ObserveGattCharacteristicUseCase;
import com.jsh.wirelessexplorer.core.domain.ble.ReadGattCharacteristicUseCase;
import com.jsh.wirelessexplorer.core.domain.ble.ScanBleDevicesUseCase;
import com.jsh.wirelessexplorer.core.domain.ble.WriteGattCharacteristicUseCase;
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
public final class BleViewModel_Factory implements Factory<BleViewModel> {
  private final Provider<ScanBleDevicesUseCase> scanBleDevicesProvider;

  private final Provider<ConnectBleDeviceUseCase> connectBleDeviceProvider;

  private final Provider<DisconnectBleDeviceUseCase> disconnectBleDeviceProvider;

  private final Provider<ReadGattCharacteristicUseCase> readGattCharacteristicProvider;

  private final Provider<WriteGattCharacteristicUseCase> writeGattCharacteristicProvider;

  private final Provider<ObserveGattCharacteristicUseCase> observeGattCharacteristicProvider;

  private BleViewModel_Factory(Provider<ScanBleDevicesUseCase> scanBleDevicesProvider,
      Provider<ConnectBleDeviceUseCase> connectBleDeviceProvider,
      Provider<DisconnectBleDeviceUseCase> disconnectBleDeviceProvider,
      Provider<ReadGattCharacteristicUseCase> readGattCharacteristicProvider,
      Provider<WriteGattCharacteristicUseCase> writeGattCharacteristicProvider,
      Provider<ObserveGattCharacteristicUseCase> observeGattCharacteristicProvider) {
    this.scanBleDevicesProvider = scanBleDevicesProvider;
    this.connectBleDeviceProvider = connectBleDeviceProvider;
    this.disconnectBleDeviceProvider = disconnectBleDeviceProvider;
    this.readGattCharacteristicProvider = readGattCharacteristicProvider;
    this.writeGattCharacteristicProvider = writeGattCharacteristicProvider;
    this.observeGattCharacteristicProvider = observeGattCharacteristicProvider;
  }

  @Override
  public BleViewModel get() {
    return newInstance(scanBleDevicesProvider.get(), connectBleDeviceProvider.get(), disconnectBleDeviceProvider.get(), readGattCharacteristicProvider.get(), writeGattCharacteristicProvider.get(), observeGattCharacteristicProvider.get());
  }

  public static BleViewModel_Factory create(Provider<ScanBleDevicesUseCase> scanBleDevicesProvider,
      Provider<ConnectBleDeviceUseCase> connectBleDeviceProvider,
      Provider<DisconnectBleDeviceUseCase> disconnectBleDeviceProvider,
      Provider<ReadGattCharacteristicUseCase> readGattCharacteristicProvider,
      Provider<WriteGattCharacteristicUseCase> writeGattCharacteristicProvider,
      Provider<ObserveGattCharacteristicUseCase> observeGattCharacteristicProvider) {
    return new BleViewModel_Factory(scanBleDevicesProvider, connectBleDeviceProvider, disconnectBleDeviceProvider, readGattCharacteristicProvider, writeGattCharacteristicProvider, observeGattCharacteristicProvider);
  }

  public static BleViewModel newInstance(ScanBleDevicesUseCase scanBleDevices,
      ConnectBleDeviceUseCase connectBleDevice, DisconnectBleDeviceUseCase disconnectBleDevice,
      ReadGattCharacteristicUseCase readGattCharacteristic,
      WriteGattCharacteristicUseCase writeGattCharacteristic,
      ObserveGattCharacteristicUseCase observeGattCharacteristic) {
    return new BleViewModel(scanBleDevices, connectBleDevice, disconnectBleDevice, readGattCharacteristic, writeGattCharacteristic, observeGattCharacteristic);
  }
}
