package com.jsh.wirelessexplorer.domain.model

data class TechDistribution(
    val techType: TechType,
    val count: Int,
    val percentage: Float,
)
