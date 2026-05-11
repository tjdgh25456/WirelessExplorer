package com.jsh.wirelessexplorer.core.domain.history

import javax.inject.Inject

class ToggleFavoriteUseCaseImpl @Inject constructor(
    private val repository: DeviceHistoryRepository,
) : ToggleFavoriteUseCase {
    /** 즐겨찾기 상태를 반전시킨다. */
    override suspend operator fun invoke(id: Long) {
        val device = repository.getById(id) ?: return
        repository.updateFavorite(id, !device.isFavorite)
    }
}
