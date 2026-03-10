package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.UserSettings
import com.jsh.wirelessexplorer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    operator fun invoke(): Flow<UserSettings> = repository.observeSettings()
}
