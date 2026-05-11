package com.jsh.wirelessexplorer.core.domain.nfc

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNfcEnabledUseCaseImpl @Inject constructor(
    private val repository: NfcRepository,
) : ObserveNfcEnabledUseCase {
    override operator fun invoke(): Flow<Boolean> = repository.observeNfcEnabled()
}
