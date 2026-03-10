package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.repository.DeviceHistoryRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: DeviceHistoryRepository,
) {
    /** 즐겨찾기 상태를 반전시킨다. */
    suspend operator fun invoke(id: Long) {
        val device = repository.getById(id) ?: return
        repository.updateFavorite(id, !device.isFavorite)
    }
}
