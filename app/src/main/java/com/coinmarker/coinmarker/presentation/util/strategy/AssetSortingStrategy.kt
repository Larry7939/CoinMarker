package com.coinmarker.coinmarker.presentation.util.strategy

import com.coinmarker.coinmarker.data.model.AssetDto
import com.coinmarker.coinmarker.presentation.util.type.SortingType

interface AssetSortingStrategy {
    var sortingType: SortingType
    fun sort(items:List<AssetDto>):List<AssetDto>
    fun strategyWithSortingType():AssetSortingStrategy
}