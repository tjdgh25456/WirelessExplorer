package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.repository.UwbRepository
import javax.inject.Inject

class CheckUwbSupportUseCase @Inject constructor(
    private val repository: UwbRepository,
) {
    operator fun invoke(): Boolean = repository.isSupported()
}
