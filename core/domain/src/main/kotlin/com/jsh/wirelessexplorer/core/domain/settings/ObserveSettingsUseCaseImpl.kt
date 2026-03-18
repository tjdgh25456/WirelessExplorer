package com.jsh.wirelessexplorer.core.domain.settings

import com.jsh.wirelessexplorer.core.model.UserSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSettingsUseCaseImpl @Inject constructor(
    private val repository: SettingsRepository,
) : ObserveSettingsUseCase {
    override operator fun invoke(): Flow<UserSettings> = repository.observeSettings()
}
