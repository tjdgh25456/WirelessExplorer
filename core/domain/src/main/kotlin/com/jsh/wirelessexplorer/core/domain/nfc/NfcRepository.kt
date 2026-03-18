package com.jsh.wirelessexplorer.core.domain.nfc

import com.jsh.wirelessexplorer.core.model.NfcTagData
import kotlinx.coroutines.flow.Flow

interface NfcRepository {
    fun observeNfcTag(): Flow<NfcTagData>
    fun isNfcEnabled(): Boolean
    fun isNfcSupported(): Boolean
    fun observeNfcEnabled(): Flow<Boolean>
}
