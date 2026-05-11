package com.jsh.wirelessexplorer.core.domain.wifi

import com.jsh.wirelessexplorer.core.model.WifiConnectionInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWifiConnectionUseCaseImpl @Inject constructor(
    private val repository: WifiRepository,
) : ObserveWifiConnectionUseCase {
    override operator fun invoke(): Flow<WifiConnectionInfo?> = repository.observeConnectionInfo()
}
