package com.jsh.wirelessexplorer.feature.nfc

import com.jsh.wirelessexplorer.core.model.NfcState

data class NfcUiState(
    val state: NfcState = NfcState.Waiting,
    val isNfcSupported: Boolean = true,
    val isNfcEnabled: Boolean = true,
    val showRawHex: Boolean = false,
)
