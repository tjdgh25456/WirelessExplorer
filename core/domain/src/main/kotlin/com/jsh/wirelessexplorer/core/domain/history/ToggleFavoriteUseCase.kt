package com.jsh.wirelessexplorer.core.domain.history

interface ToggleFavoriteUseCase {
    suspend operator fun invoke(id: Long)
}
