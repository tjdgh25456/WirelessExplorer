package com.jsh.wirelessexplorer.core.model

data class BleDevice(
    val name: String,
    val address: String,
    val rssi: Int,
    val deviceType: BleDeviceType,
    val bondState: BleBondState,
    val lastSeen: Long,
    val txPower: Int? = null,
    val serviceUuids: List<String> = emptyList(),
    val manufacturerData: Map<Int, ByteArray> = emptyMap(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BleDevice) return false
        return address == other.address && rssi == other.rssi && name == other.name &&
            deviceType == other.deviceType && bondState == other.bondState &&
            lastSeen == other.lastSeen && txPower == other.txPower &&
            serviceUuids == other.serviceUuids &&
            manufacturerData.keys == other.manufacturerData.keys &&
            manufacturerData.all { (k, v) -> other.manufacturerData[k].contentEquals(v) }
    }

    override fun hashCode(): Int {
        var result = address.hashCode()
        result = 31 * result + rssi
        result = 31 * result + name.hashCode()
        result = 31 * result + (txPower ?: 0)
        return result
    }
}
