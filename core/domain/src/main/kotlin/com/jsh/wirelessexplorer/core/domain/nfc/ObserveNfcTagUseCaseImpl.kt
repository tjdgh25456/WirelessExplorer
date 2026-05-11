package com.jsh.wirelessexplorer.core.domain.nfc

import com.jsh.wirelessexplorer.core.model.NfcTagData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNfcTagUseCaseImpl @Inject constructor(
    private val repository: NfcRepository,
) : ObserveNfcTagUseCase {
    override operator fun invoke(): Flow<NfcTagData> = repository.observeNfcTag()
}
