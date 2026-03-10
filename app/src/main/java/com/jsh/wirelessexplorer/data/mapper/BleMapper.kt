package com.jsh.wirelessexplorer.data.mapper

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.le.ScanResult
import com.jsh.wirelessexplorer.domain.model.BleBondState
import com.jsh.wirelessexplorer.domain.model.BleDevice
import com.jsh.wirelessexplorer.domain.model.BleDeviceType
import com.jsh.wirelessexplorer.domain.model.GattCharacteristicInfo
import com.jsh.wirelessexplorer.domain.model.GattProperty
import com.jsh.wirelessexplorer.domain.model.GattServiceInfo

/** Bluetooth SIG 표준 UUID의 짧은 접두사 길이 (예: "00001800") */
private const val UUID_SHORT_PREFIX_LENGTH = 8

// 표준 Bluetooth SIG 서비스 UUID 매핑
private val KNOWN_SERVICES = mapOf(
    "00001800" to "Generic Access",
    "00001801" to "Generic Attribute",
    "0000180a" to "Device Information",
    "0000180d" to "Heart Rate",
    "0000180f" to "Battery Service",
    "00001810" to "Blood Pressure",
    "00001816" to "Cycling Speed and Cadence",
    "00001818" to "Cycling Power",
    "0000181a" to "Environmental Sensing",
    "0000181c" to "User Data",
    "0000181d" to "Weight Scale",
    "00001812" to "Human Interface Device",
    "00001808" to "Glucose",
    "00001809" to "Health Thermometer",
    "00001802" to "Immediate Alert",
    "00001803" to "Link Loss",
    "00001804" to "Tx Power",
    "00001811" to "Alert Notification",
    "0000fee0" to "Mi Band Service",
    "0000feaa" to "Eddystone",
)

// 표준 Bluetooth SIG 특성 UUID 매핑
private val KNOWN_CHARACTERISTICS = mapOf(
    "00002a00" to "Device Name",
    "00002a01" to "Appearance",
    "00002a04" to "Peripheral Preferred Connection Parameters",
    "00002a19" to "Battery Level",
    "00002a24" to "Model Number String",
    "00002a25" to "Serial Number String",
    "00002a26" to "Firmware Revision String",
    "00002a27" to "Hardware Revision String",
    "00002a28" to "Software Revision String",
    "00002a29" to "Manufacturer Name String",
    "00002a37" to "Heart Rate Measurement",
    "00002a38" to "Body Sensor Location",
    "00002a39" to "Heart Rate Control Point",
    "00002a05" to "Service Changed",
)

@android.annotation.SuppressLint("MissingPermission")
internal fun ScanResult.toBleDevice(): BleDevice = BleDevice(
    name = device.name ?: "Unknown",
    address = device.address,
    rssi = rssi,
    deviceType = device.type.toBleDeviceType(),
    bondState = device.bondState.toBleBondState(),
    lastSeen = System.currentTimeMillis(),
)

internal fun Int.toBleDeviceType(): BleDeviceType = when (this) {
    BluetoothDevice.DEVICE_TYPE_LE -> BleDeviceType.PERIPHERAL
    BluetoothDevice.DEVICE_TYPE_CLASSIC -> BleDeviceType.AUDIO
    BluetoothDevice.DEVICE_TYPE_DUAL -> BleDeviceType.PHONE
    else -> BleDeviceType.UNKNOWN
}

internal fun Int.toBleBondState(): BleBondState = when (this) {
    BluetoothDevice.BOND_BONDED -> BleBondState.BONDED
    BluetoothDevice.BOND_BONDING -> BleBondState.BONDING
    else -> BleBondState.NONE
}

internal fun BluetoothGattService.toGattServiceInfo(): GattServiceInfo {
    val uuidStr = uuid.toString().lowercase()
    val shortUuid = uuidStr.take(UUID_SHORT_PREFIX_LENGTH)
    return GattServiceInfo(
        uuid = uuidStr,
        name = KNOWN_SERVICES[shortUuid] ?: "Unknown Service",
        characteristics = characteristics.map { it.toGattCharacteristicInfo(uuidStr) },
    )
}

internal fun BluetoothGattCharacteristic.toGattCharacteristicInfo(
    serviceUuid: String,
): GattCharacteristicInfo {
    val uuidStr = uuid.toString().lowercase()
    val shortUuid = uuidStr.take(UUID_SHORT_PREFIX_LENGTH)
    return GattCharacteristicInfo(
        uuid = uuidStr,
        serviceUuid = serviceUuid,
        name = KNOWN_CHARACTERISTICS[shortUuid] ?: "Unknown Characteristic",
        properties = properties.toGattProperties(),
        value = null,
    )
}

internal fun Int.toGattProperties(): Set<GattProperty> = buildSet {
    if (this@toGattProperties and BluetoothGattCharacteristic.PROPERTY_READ != 0) add(GattProperty.READ)
    if (this@toGattProperties and BluetoothGattCharacteristic.PROPERTY_WRITE != 0) add(GattProperty.WRITE)
    if (this@toGattProperties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE != 0) add(GattProperty.WRITE_NO_RESPONSE)
    if (this@toGattProperties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) add(GattProperty.NOTIFY)
    if (this@toGattProperties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) add(GattProperty.INDICATE)
}
