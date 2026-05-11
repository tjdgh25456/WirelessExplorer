package com.jsh.wirelessexplorer.core.model

data class BleAdvertisingData(
    val localName: String?,
    val flags: Int?,
    val primaryPhy: BlePhy,
    val secondaryPhy: BlePhy,
    val advertisingSid: Int?,
    val dataStatus: BleDataStatus,
    val serviceData: Map<String, ByteArray>,
    val rawBytes: ByteArray?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BleAdvertisingData) return false
        return localName == other.localName && flags == other.flags &&
            primaryPhy == other.primaryPhy && secondaryPhy == other.secondaryPhy &&
            advertisingSid == other.advertisingSid && dataStatus == other.dataStatus &&
            serviceData.keys == other.serviceData.keys &&
            serviceData.all { (k, v) -> other.serviceData[k].contentEquals(v) } &&
            rawBytes.contentEquals(other.rawBytes)
    }

    override fun hashCode(): Int {
        var result = (localName?.hashCode() ?: 0)
        result = 31 * result + (flags ?: 0)
        result = 31 * result + primaryPhy.hashCode()
        result = 31 * result + secondaryPhy.hashCode()
        result = 31 * result + (advertisingSid ?: 0)
        result = 31 * result + dataStatus.hashCode()
        result = 31 * result + serviceData.entries.fold(0) { acc, (k, v) ->
            acc xor (k.hashCode() * 31 + v.contentHashCode())
        }
        result = 31 * result + (rawBytes?.contentHashCode() ?: 0)
        return result
    }
}
