package com.jsh.wirelessexplorer.data.repository

import android.app.Activity
import com.jsh.wirelessexplorer.data.datasource.NfcReader
import com.jsh.wirelessexplorer.domain.model.NfcTagData
import com.jsh.wirelessexplorer.domain.repository.NfcRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber
import javax.inject.Inject

class NfcRepositoryImpl @Inject constructor(
    private val nfcReader: NfcReader,
) : NfcRepository {

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
}
