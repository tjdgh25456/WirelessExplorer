package com.jsh.wirelessexplorer.core.domain.wifi

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWifiEnabledUseCaseImpl @Inject constructor(
    private val repository: WifiRepository,
) : ObserveWifiEnabledUseCase {
    override operator fun invoke(): Flow<Boolean> = repository.observeWifiEnabled()
}
