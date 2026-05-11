package com.jsh.wirelessexplorer.core.model

data class NfcTagData(
    val id: String,
    val techList: List<String>,
    val ndefRecords: List<NdefRecord>,
    val rawBytes: ByteArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NfcTagData) return false
        return id == other.id &&
            techList == other.techList &&
            ndefRecords == other.ndefRecords &&
            rawBytes.contentEquals(other.rawBytes)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + techList.hashCode()
        result = 31 * result + ndefRecords.hashCode()
        result = 31 * result + (rawBytes?.contentHashCode() ?: 0)
        return result
    }
}
