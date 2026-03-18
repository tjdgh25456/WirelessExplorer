package com.jsh.wirelessexplorer.core.domain.nfc

import kotlinx.coroutines.flow.Flow

interface ObserveNfcEnabledUseCase {
    operator fun invoke(): Flow<Boolean>
}
