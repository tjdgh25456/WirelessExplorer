package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.WifiConnectionInfo
import com.jsh.wirelessexplorer.domain.repository.WifiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWifiConnectionUseCase @Inject constructor(
    private val repository: WifiRepository,
) {
    operator fun invoke(): Flow<WifiConnectionInfo?> = repository.observeConnectionInfo()
}
