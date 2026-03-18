package com.jsh.wirelessexplorer.core.domain.uwb

import javax.inject.Inject

class CheckUwbSupportUseCaseImpl @Inject constructor(
    private val repository: UwbRepository,
) : CheckUwbSupportUseCase {
    override operator fun invoke(): Boolean = repository.isSupported()
}
