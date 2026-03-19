package com.jsh.wirelessexplorer.core.data.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import com.jsh.wirelessexplorer.core.model.BleBondState
import com.jsh.wirelessexplorer.core.model.BleDeviceType
import com.jsh.wirelessexplorer.core.model.GattProperty
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BleMapperTest {

    @Test
    fun toBleDeviceType_leDevice_returnsPeripheral() {
        assertEquals(BleDeviceType.PERIPHERAL, BluetoothDevice.DEVICE_TYPE_LE.toBleDeviceType())
    }

    @Test
    fun toBleDeviceType_classicDevice_returnsAudio() {
        assertEquals(BleDeviceType.AUDIO, BluetoothDevice.DEVICE_TYPE_CLASSIC.toBleDeviceType())
    }

    @Test
    fun toBleDeviceType_dualDevice_returnsPhone() {
        assertEquals(BleDeviceType.PHONE, BluetoothDevice.DEVICE_TYPE_DUAL.toBleDeviceType())
    }

    @Test
    fun toBleDeviceType_unknownValue_returnsUnknown() {
        assertEquals(BleDeviceType.UNKNOWN, 999.toBleDeviceType())
    }

    @Test
    fun toBleBondState_bonded_returnsBonded() {
        assertEquals(BleBondState.BONDED, BluetoothDevice.BOND_BONDED.toBleBondState())
    }

    @Test
    fun toBleBondState_bonding_returnsBonding() {
        assertEquals(BleBondState.BONDING, BluetoothDevice.BOND_BONDING.toBleBondState())
    }

    @Test
    fun toBleBondState_none_returnsNone() {
        assertEquals(BleBondState.NONE, BluetoothDevice.BOND_NONE.toBleBondState())
    }

    @Test
    fun toGattProperties_readOnly_returnsReadSet() {
        val properties = BluetoothGattCharacteristic.PROPERTY_READ.toGattProperties()
        assertEquals(setOf(GattProperty.READ), properties)
    }

    @Test
    fun toGattProperties_readAndWrite_returnsBothProperties() {
        val combined = BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_WRITE
        val properties = combined.toGattProperties()
        assertEquals(setOf(GattProperty.READ, GattProperty.WRITE), properties)
    }

    @Test
    fun toGattProperties_allProperties_returnsFullSet() {
        val all = BluetoothGattCharacteristic.PROPERTY_READ or
            BluetoothGattCharacteristic.PROPERTY_WRITE or
            BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE or
            BluetoothGattCharacteristic.PROPERTY_NOTIFY or
            BluetoothGattCharacteristic.PROPERTY_INDICATE
        val properties = all.toGattProperties()
        assertEquals(GattProperty.entries.toSet(), properties)
    }

    @Test
    fun toGattProperties_noProperties_returnsEmptySet() {
        val properties = 0.toGattProperties()
        assertTrue(properties.isEmpty())
    }
}
