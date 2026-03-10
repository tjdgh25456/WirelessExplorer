package com.jsh.wirelessexplorer.presentation.settings

import com.jsh.wirelessexplorer.domain.model.UserSettings

data class SettingsUiState(
    val settings: UserSettings = UserSettings(),
    val showClearHistoryDialog: Boolean = false,
    val appVersion: String = "",
)
