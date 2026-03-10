package com.jsh.wirelessexplorer.domain.model

sealed interface NdefRecord {

    data class TextRecord(
        val language: String,
        val text: String,
    ) : NdefRecord

    data class UriRecord(
        val uri: String,
    ) : NdefRecord

    data class MimeRecord(
        val mimeType: String,
        val data: ByteArray,
    ) : NdefRecord {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is MimeRecord) return false
            return mimeType == other.mimeType && data.contentEquals(other.data)
        }

        override fun hashCode(): Int = 31 * mimeType.hashCode() + data.contentHashCode()
    }

    data class ExternalRecord(
        val domain: String,
        val type: String,
        val data: ByteArray,
    ) : NdefRecord {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ExternalRecord) return false
            return domain == other.domain && type == other.type && data.contentEquals(other.data)
        }

        override fun hashCode(): Int {
            var result = domain.hashCode()
            result = 31 * result + type.hashCode()
            result = 31 * result + data.contentHashCode()
            return result
        }
    }

    data class UnknownRecord(
        val payload: ByteArray,
    ) : NdefRecord {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is UnknownRecord) return false
            return payload.contentEquals(other.payload)
        }

        override fun hashCode(): Int = payload.contentHashCode()
    }
}
