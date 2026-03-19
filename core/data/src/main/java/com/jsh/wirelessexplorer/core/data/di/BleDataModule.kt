package com.jsh.wirelessexplorer.core.data.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.jsh.wirelessexplorer.core.data.ble.BleRepositoryImpl
import com.jsh.wirelessexplorer.core.domain.ble.BleRepository
import com.jsh.wirelessexplorer.core.domain.ble.ConnectBleDeviceUseCase
import com.jsh.wirelessexplorer.core.domain.ble.ConnectBleDeviceUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.ble.DisconnectBleDeviceUseCase
import com.jsh.wirelessexplorer.core.domain.ble.DisconnectBleDeviceUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.ble.GetBluetoothEnabledUseCase
import com.jsh.wirelessexplorer.core.domain.ble.GetScannedBleDeviceUseCase
import com.jsh.wirelessexplorer.core.domain.ble.GetScannedBleDeviceUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.ble.GetBluetoothEnabledUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.ble.ObserveBleConnectionsUseCase
import com.jsh.wirelessexplorer.core.domain.ble.ObserveBleConnectionsUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.ble.ObserveBluetoothEnabledUseCase
import com.jsh.wirelessexplorer.core.domain.ble.ObserveBluetoothEnabledUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.ble.ObserveGattCharacteristicUseCase
import com.jsh.wirelessexplorer.core.domain.ble.ObserveGattCharacteristicUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.ble.ReadGattCharacteristicUseCase
import com.jsh.wirelessexplorer.core.domain.ble.ReadGattCharacteristicUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.ble.ScanBleDevicesUseCase
import com.jsh.wirelessexplorer.core.domain.ble.ScanBleDevicesUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.ble.WriteGattCharacteristicUseCase
import com.jsh.wirelessexplorer.core.domain.ble.WriteGattCharacteristicUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BleDataModule {

    @Binds
    @Singleton
    abstract fun bindBleRepository(impl: BleRepositoryImpl): BleRepository

    @Binds
    abstract fun bindScanBleDevices(impl: ScanBleDevicesUseCaseImpl): ScanBleDevicesUseCase

    @Binds
    abstract fun bindConnectBleDevice(impl: ConnectBleDeviceUseCaseImpl): ConnectBleDeviceUseCase

    @Binds
    abstract fun bindDisconnectBleDevice(impl: DisconnectBleDeviceUseCaseImpl): DisconnectBleDeviceUseCase

    @Binds
    abstract fun bindGetBluetoothEnabled(impl: GetBluetoothEnabledUseCaseImpl): GetBluetoothEnabledUseCase

    @Binds
    abstract fun bindObserveBluetoothEnabled(impl: ObserveBluetoothEnabledUseCaseImpl): ObserveBluetoothEnabledUseCase

    @Binds
    abstract fun bindObserveBleConnections(impl: ObserveBleConnectionsUseCaseImpl): ObserveBleConnectionsUseCase

    @Binds
    abstract fun bindObserveGattCharacteristic(impl: ObserveGattCharacteristicUseCaseImpl): ObserveGattCharacteristicUseCase

    @Binds
    abstract fun bindReadGattCharacteristic(impl: ReadGattCharacteristicUseCaseImpl): ReadGattCharacteristicUseCase

    @Binds
    abstract fun bindWriteGattCharacteristic(impl: WriteGattCharacteristicUseCaseImpl): WriteGattCharacteristicUseCase

    @Binds
    abstract fun bindGetScannedBleDevice(impl: GetScannedBleDeviceUseCaseImpl): GetScannedBleDeviceUseCase

    companion object {

        @Provides
        @Singleton
        fun provideBluetoothAdapter(@ApplicationContext context: Context): BluetoothAdapter? {
            val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            return manager.adapter
        }
    }
}
