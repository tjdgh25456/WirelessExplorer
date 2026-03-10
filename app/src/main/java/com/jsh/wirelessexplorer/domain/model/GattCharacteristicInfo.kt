package com.jsh.wirelessexplorer.domain.model

data class GattCharacteristicInfo(
    val uuid: String,
    val serviceUuid: String,
    val name: String,
    val properties: Set<GattProperty>,
    val value: ByteArray?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GattCharacteristicInfo) return false
        return uuid == other.uuid &&
            serviceUuid == other.serviceUuid &&
            name == other.name &&
            properties == other.properties &&
            value.contentEquals(other.value)
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + serviceUuid.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + properties.hashCode()
        result = 31 * result + (value?.contentHashCode() ?: 0)
        return result
    }
}
