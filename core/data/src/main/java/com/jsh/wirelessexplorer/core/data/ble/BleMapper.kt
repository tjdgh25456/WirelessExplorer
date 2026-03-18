package com.jsh.wirelessexplorer.core.data.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.le.ScanResult
import com.jsh.wirelessexplorer.core.model.BleAdvertisingData
import com.jsh.wirelessexplorer.core.model.BleBondState
import com.jsh.wirelessexplorer.core.model.BleDataStatus
import com.jsh.wirelessexplorer.core.model.BleDevice
import com.jsh.wirelessexplorer.core.model.BleDeviceType
import com.jsh.wirelessexplorer.core.model.BlePhy
import com.jsh.wirelessexplorer.core.model.GattCharacteristicInfo
import com.jsh.wirelessexplorer.core.model.GattDescriptorInfo
import com.jsh.wirelessexplorer.core.model.GattProperty
import com.jsh.wirelessexplorer.core.model.GattServiceInfo

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
internal fun ScanResult.toBleDevice(now: Long): BleDevice {
    val record = scanRecord
    val serviceUuids = record?.serviceUuids?.map { it.uuid.toString().lowercase() } ?: emptyList()
    val mfrData = buildMap {
        val sparse = record?.manufacturerSpecificData
        if (sparse != null) {
            for (i in 0 until sparse.size()) {
                put(sparse.keyAt(i), sparse.valueAt(i))
            }
        }
    }
    val txPower = record?.txPowerLevel?.takeIf { it != Int.MIN_VALUE }

    return BleDevice(
        name = device.name ?: "Unknown",
        address = device.address,
        rssi = rssi,
        deviceType = device.type.toBleDeviceType(),
        bondState = device.bondState.toBleBondState(),
        lastSeen = now,
        txPower = txPower,
        serviceUuids = serviceUuids,
        manufacturerData = mfrData,
        advertisingData = toBleAdvertisingData(),
    )
}

/** ScanRecord.getAdvertiseFlags()가 Flags AD type 부재 시 반환하는 센티널 */
private const val ADVERTISE_FLAGS_NOT_PRESENT = -1

@SuppressLint("MissingPermission")
private fun ScanResult.toBleAdvertisingData(): BleAdvertisingData {
    val record = scanRecord
    val svcData = buildMap {
        record?.serviceData?.forEach { (uuid, bytes) ->
            put(uuid.uuid.toString().lowercase(), bytes)
        }
    }
    return BleAdvertisingData(
        localName = record?.deviceName,
        flags = record?.advertiseFlags?.takeIf { it != ADVERTISE_FLAGS_NOT_PRESENT },
        primaryPhy = primaryPhy.toBlePhy(),
        secondaryPhy = secondaryPhy.toBlePhy(),
        advertisingSid = advertisingSid.takeIf { it != ScanResult.SID_NOT_PRESENT },
        dataStatus = dataStatus.toBleDataStatus(),
        serviceData = svcData,
        rawBytes = record?.bytes,
    )
}

private fun Int.toBlePhy(): BlePhy = when (this) {
    BluetoothDevice.PHY_LE_1M -> BlePhy.PHY_1M
    BluetoothDevice.PHY_LE_2M -> BlePhy.PHY_2M
    BluetoothDevice.PHY_LE_CODED -> BlePhy.PHY_CODED
    else -> BlePhy.UNKNOWN
}

private fun Int.toBleDataStatus(): BleDataStatus = when (this) {
    ScanResult.DATA_COMPLETE -> BleDataStatus.COMPLETE
    ScanResult.DATA_TRUNCATED -> BleDataStatus.TRUNCATED
    1 -> BleDataStatus.INCOMPLETE // ScanResult에 상수 미정의, Extended Advertising 분할 수신 시
    else -> BleDataStatus.UNKNOWN
}

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

// 표준 Bluetooth SIG Descriptor UUID 매핑
private val KNOWN_DESCRIPTORS = mapOf(
    "00002900" to "Characteristic Extended Properties",
    "00002901" to "Characteristic User Description",
    "00002902" to "Client Characteristic Configuration",
    "00002903" to "Server Characteristic Configuration",
    "00002904" to "Characteristic Presentation Format",
    "00002905" to "Characteristic Aggregate Format",
)

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
        descriptors = descriptors.map { it.toGattDescriptorInfo() },
    )
}

internal fun BluetoothGattDescriptor.toGattDescriptorInfo(): GattDescriptorInfo {
    val uuidStr = uuid.toString().lowercase()
    val shortUuid = uuidStr.take(UUID_SHORT_PREFIX_LENGTH)
    return GattDescriptorInfo(
        uuid = uuidStr,
        name = KNOWN_DESCRIPTORS[shortUuid] ?: "Unknown Descriptor",
    )
}

internal fun Int.toGattProperties(): Set<GattProperty> = buildSet {
    if (this@toGattProperties and BluetoothGattCharacteristic.PROPERTY_READ != 0) add(GattProperty.READ)
    if (this@toGattProperties and BluetoothGattCharacteristic.PROPERTY_WRITE != 0) add(GattProperty.WRITE)
    if (this@toGattProperties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE != 0) add(GattProperty.WRITE_NO_RESPONSE)
    if (this@toGattProperties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) add(GattProperty.NOTIFY)
    if (this@toGattProperties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) add(GattProperty.INDICATE)
}
