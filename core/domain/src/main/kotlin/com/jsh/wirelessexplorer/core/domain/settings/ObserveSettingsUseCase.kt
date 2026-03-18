package com.jsh.wirelessexplorer.core.domain.settings

import com.jsh.wirelessexplorer.core.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface ObserveSettingsUseCase {
    operator fun invoke(): Flow<UserSettings>
}
