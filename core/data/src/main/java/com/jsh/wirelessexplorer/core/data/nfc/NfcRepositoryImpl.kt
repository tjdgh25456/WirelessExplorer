package com.jsh.wirelessexplorer.core.data.nfc

import android.app.Activity
import android.nfc.NfcAdapter
import com.jsh.wirelessexplorer.core.model.NfcTagData
import com.jsh.wirelessexplorer.core.data.nfc.NfcReaderController
import com.jsh.wirelessexplorer.core.domain.nfc.NfcRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class NfcRepositoryImpl @Inject constructor(
    private val nfcReader: NfcReader,
) : NfcRepository, NfcReaderController {

    private val tagFlow = MutableSharedFlow<NfcTagData>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override fun observeNfcTag(): Flow<NfcTagData> = tagFlow.asSharedFlow()

    override fun enableReader(activity: Activity) {
        nfcReader.enableReader(activity) { tag ->
            try {
                val parsed = nfcReader.parseTag(tag)
                tagFlow.tryEmit(parsed)
            } catch (e: Exception) {
                Timber.e(e, "NFC 태그 파싱 중 오류 발생")
            }
        }
    }

    override fun disableReader(activity: Activity) {
        nfcReader.disableReader(activity)
    }

    override fun isNfcEnabled(): Boolean = nfcReader.isEnabled()

    override fun isNfcSupported(): Boolean = nfcReader.isSupported()

    override fun observeNfcEnabled(): Flow<Boolean> = flow {
        // NFC 상태 변경 BroadcastReceiver가 없으므로 폴링 방식 사용
        var lastState = isNfcEnabled()
        emit(lastState)
        while (true) {
            delay(NFC_POLL_INTERVAL_MS)
            val current = isNfcEnabled()
            if (current != lastState) {
                lastState = current
                emit(current)
            }
        }
    }

    companion object {
        private const val NFC_POLL_INTERVAL_MS = 2_000L
    }
}
