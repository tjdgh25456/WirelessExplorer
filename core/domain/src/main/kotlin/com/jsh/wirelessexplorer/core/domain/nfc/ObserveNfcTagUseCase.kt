package com.jsh.wirelessexplorer.core.domain.nfc

import com.jsh.wirelessexplorer.core.model.NfcTagData
import kotlinx.coroutines.flow.Flow

interface ObserveNfcTagUseCase {
    operator fun invoke(): Flow<NfcTagData>
}
