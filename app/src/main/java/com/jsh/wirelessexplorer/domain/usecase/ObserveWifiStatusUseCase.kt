package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.WifiStatus
import com.jsh.wirelessexplorer.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWifiStatusUseCase @Inject constructor(
    private val repository: DashboardRepository,
) {
    operator fun invoke(): Flow<WifiStatus?> = repository.observeWifiStatus()
}
