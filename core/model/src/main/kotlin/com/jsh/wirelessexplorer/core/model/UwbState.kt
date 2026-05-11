package com.jsh.wirelessexplorer.core.model

sealed interface UwbState {
    data object NotSupported : UwbState
    data object Idle : UwbState
    data object Searching : UwbState
    data class Ranging(val result: UwbRangingResult) : UwbState
    data class Error(val message: String) : UwbState
}
