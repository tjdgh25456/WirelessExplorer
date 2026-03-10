package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.NfcTagData
import com.jsh.wirelessexplorer.domain.repository.NfcRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNfcTagUseCase @Inject constructor(
    private val repository: NfcRepository,
) {
    operator fun invoke(): Flow<NfcTagData> = repository.observeNfcTag()
}
