package com.jsh.wirelessexplorer.core.domain.wifi

import com.jsh.wirelessexplorer.core.model.WifiStatus
import com.jsh.wirelessexplorer.core.domain.dashboard.DashboardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWifiStatusUseCaseImpl @Inject constructor(
    private val repository: DashboardRepository,
) : ObserveWifiStatusUseCase {
    override operator fun invoke(): Flow<WifiStatus?> = repository.observeWifiStatus()
}
