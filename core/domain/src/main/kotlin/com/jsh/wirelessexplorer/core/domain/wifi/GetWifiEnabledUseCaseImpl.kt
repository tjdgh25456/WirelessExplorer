package com.jsh.wirelessexplorer.core.domain.wifi

import javax.inject.Inject

class GetWifiEnabledUseCaseImpl @Inject constructor(
    private val wifiRepository: WifiRepository,
) : GetWifiEnabledUseCase {
    override operator fun invoke(): Boolean = wifiRepository.isWifiEnabled()
}
