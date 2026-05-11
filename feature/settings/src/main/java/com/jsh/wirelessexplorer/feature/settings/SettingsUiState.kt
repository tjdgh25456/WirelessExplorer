package com.jsh.wirelessexplorer.feature.settings

import com.jsh.wirelessexplorer.core.model.UserSettings

data class SettingsUiState(
    val settings: UserSettings = UserSettings(),
    val showClearHistoryDialog: Boolean = false,
    val appVersion: String = "",
)
