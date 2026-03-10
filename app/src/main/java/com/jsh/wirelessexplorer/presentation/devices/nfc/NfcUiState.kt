package com.jsh.wirelessexplorer.presentation.devices.nfc

import com.jsh.wirelessexplorer.domain.model.NfcState

data class NfcUiState(
    val state: NfcState = NfcState.Waiting,
    val isNfcSupported: Boolean = true,
    val isNfcEnabled: Boolean = true,
)
