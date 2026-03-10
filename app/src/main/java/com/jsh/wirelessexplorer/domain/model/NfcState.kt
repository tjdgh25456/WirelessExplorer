package com.jsh.wirelessexplorer.domain.model

sealed interface NfcState {
    data object Waiting : NfcState
    data class TagDetected(val tag: NfcTagData) : NfcState
    data class Error(val message: String) : NfcState
    data object NotSupported : NfcState
    data object Disabled : NfcState
}
