package com.jsh.wirelessexplorer.core.data.nfc

import android.annotation.SuppressLint
import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import com.jsh.wirelessexplorer.core.model.NdefRecord
import com.jsh.wirelessexplorer.core.model.NfcTagData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import java.nio.charset.Charset

private const val NFC_PRESENCE_CHECK_DELAY_MS = 250

// NFC RTD_TEXT 파싱용 비트마스크
private const val TEXT_STATUS_BYTE_MASK = 0xFF
private const val TEXT_UTF16_FLAG_MASK = 0x80
private const val TEXT_LANGUAGE_CODE_LENGTH_MASK = 0x3F

// NdefRecord 외부 타입 분리용
private const val EXTERNAL_TYPE_SPLIT_LIMIT = 2

/**
 * NFC 태그 리더기. Activity 참조가 필요하므로 Hilt 주입 대상이 아니다.
 * enableReaderMode / disableReaderMode 생명주기를 호출부에서 관리해야 한다.
 */
class NfcReader(
    private val nfcAdapter: NfcAdapter?,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    /** URI prefix byte 매핑 (NFC Forum RTD URI specification) */
    private val uriPrefixMap = mapOf(
        0x00.toByte() to "",
        0x01.toByte() to "http://www.",
        0x02.toByte() to "https://www.",
        0x03.toByte() to "http://",
        0x04.toByte() to "https://",
        0x05.toByte() to "tel:",
        0x06.toByte() to "mailto:",
        0x07.toByte() to "ftp://anonymous:anonymous@",
        0x08.toByte() to "ftp://ftp.",
        0x09.toByte() to "ftps://",
        0x0A.toByte() to "sftp://",
        0x0B.toByte() to "smb://",
        0x0C.toByte() to "nfs://",
        0x0D.toByte() to "ftp://",
        0x0E.toByte() to "dav://",
        0x0F.toByte() to "news:",
        0x10.toByte() to "telnet://",
        0x11.toByte() to "imap:",
        0x12.toByte() to "rtsp://",
        0x13.toByte() to "urn:",
        0x14.toByte() to "pop:",
        0x15.toByte() to "sip:",
        0x16.toByte() to "sips:",
        0x17.toByte() to "tftp:",
        0x18.toByte() to "btspp://",
        0x19.toByte() to "btl2cap://",
        0x1A.toByte() to "btgoep://",
        0x1B.toByte() to "tcpobex://",
        0x1C.toByte() to "irdaobex://",
        0x1D.toByte() to "file://",
        0x1E.toByte() to "urn:epc:id:",
        0x1F.toByte() to "urn:epc:tag:",
        0x20.toByte() to "urn:epc:pat:",
        0x21.toByte() to "urn:epc:raw:",
        0x22.toByte() to "urn:epc:",
        0x23.toByte() to "urn:nfc:",
    )

    fun isSupported(): Boolean = nfcAdapter != null

    fun isEnabled(): Boolean = nfcAdapter?.isEnabled == true

    @SuppressLint("MissingPermission")
    fun enableReader(activity: Activity, onTag: (Tag) -> Unit) {
        val adapter = nfcAdapter ?: run {
            Timber.w("NFC 어댑터를 사용할 수 없음")
            return
        }

        val flags = NfcAdapter.FLAG_READER_NFC_A or
            NfcAdapter.FLAG_READER_NFC_B or
            NfcAdapter.FLAG_READER_NFC_F or
            NfcAdapter.FLAG_READER_NFC_V

        val extras = Bundle().apply {
            putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, NFC_PRESENCE_CHECK_DELAY_MS)
        }

        Timber.d("NFC Reader 모드 활성화")
        adapter.enableReaderMode(activity, { tag -> onTag(tag) }, flags, extras)
    }

    fun disableReader(activity: Activity) {
        nfcAdapter?.disableReaderMode(activity)
        Timber.d("NFC Reader 모드 비활성화")
    }

    /** 태그에서 NfcTagData 도메인 모델로 변환한다. */
    fun parseTag(tag: Tag): NfcTagData {
        val tagId = tag.id.toHexString()
        val techList = tag.techList.map { it.substringAfterLast('.') }
        val records = readNdefRecords(tag)

        Timber.d("NFC 태그 감지: id=$tagId, techs=$techList, records=${records.size}개")

        return NfcTagData(
            id = tagId,
            techList = techList,
            ndefRecords = records,
            rawBytes = tag.id,
        )
    }

    /** NDEF 메시지를 파싱하여 도메인 레코드 목록을 반환한다. */
    private fun readNdefRecords(tag: Tag): List<NdefRecord> {
        val ndef = Ndef.get(tag) ?: return emptyList()
        return try {
            ndef.connect()
            val message = ndef.ndefMessage ?: return emptyList()
            message.records.mapNotNull { record -> parseNdefRecord(record) }
        } catch (e: Exception) {
            Timber.e(e, "NDEF 레코드 읽기 실패")
            emptyList()
        } finally {
            runCatching { ndef.close() }
        }
    }

    private fun parseNdefRecord(record: android.nfc.NdefRecord): NdefRecord? {
        return when (record.tnf) {
            android.nfc.NdefRecord.TNF_WELL_KNOWN -> parseWellKnownRecord(record)
            android.nfc.NdefRecord.TNF_MIME_MEDIA -> NdefRecord.MimeRecord(
                mimeType = String(record.type, Charsets.US_ASCII),
                data = record.payload,
            )
            android.nfc.NdefRecord.TNF_ABSOLUTE_URI -> NdefRecord.UriRecord(
                uri = String(record.payload, Charsets.UTF_8),
            )
            android.nfc.NdefRecord.TNF_EXTERNAL_TYPE -> {
                val fullType = String(record.type, Charsets.US_ASCII)
                val parts = fullType.split(":", limit = EXTERNAL_TYPE_SPLIT_LIMIT)
                NdefRecord.ExternalRecord(
                    domain = parts.getOrElse(0) { fullType },
                    type = parts.getOrElse(1) { "" },
                    data = record.payload,
                )
            }
            else -> {
                if (record.payload.isNotEmpty()) {
                    NdefRecord.UnknownRecord(payload = record.payload)
                } else {
                    null
                }
            }
        }
    }

    private fun parseWellKnownRecord(record: android.nfc.NdefRecord): NdefRecord? {
        val type = record.type
        return when {
            type.contentEquals(android.nfc.NdefRecord.RTD_TEXT) -> parseTextRecord(record.payload)
            type.contentEquals(android.nfc.NdefRecord.RTD_URI) -> parseUriRecord(record.payload)
            else -> NdefRecord.UnknownRecord(payload = record.payload)
        }
    }

    /**
     * RTD_TEXT 파싱. 첫 바이트 상위 비트가 인코딩, 하위 6비트가 언어 코드 길이.
     * UTF-8(bit7=0) 또는 UTF-16(bit7=1).
     */
    private fun parseTextRecord(payload: ByteArray): NdefRecord.TextRecord? {
        if (payload.isEmpty()) return null

        val statusByte = payload[0].toInt() and TEXT_STATUS_BYTE_MASK
        val isUtf16 = (statusByte and TEXT_UTF16_FLAG_MASK) != 0
        val languageCodeLength = statusByte and TEXT_LANGUAGE_CODE_LENGTH_MASK

        if (payload.size < 1 + languageCodeLength) return null

        val language = String(payload, 1, languageCodeLength, Charsets.US_ASCII)
        val textEncoding: Charset = if (isUtf16) Charsets.UTF_16 else Charsets.UTF_8
        val textOffset = 1 + languageCodeLength
        val text = String(payload, textOffset, payload.size - textOffset, textEncoding)

        return NdefRecord.TextRecord(language = language, text = text)
    }

    /** RTD_URI 파싱. 첫 바이트가 URI prefix 코드, 나머지가 URI body. */
    private fun parseUriRecord(payload: ByteArray): NdefRecord.UriRecord? {
        if (payload.isEmpty()) return null

        val prefixByte = payload[0]
        val prefix = uriPrefixMap[prefixByte] ?: ""
        val body = String(payload, 1, payload.size - 1, Charsets.UTF_8)

        return NdefRecord.UriRecord(uri = prefix + body)
    }

    private fun ByteArray.toHexString(): String =
        joinToString(separator = ":") { "%02X".format(it) }
}
