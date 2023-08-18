package com.coinmarker.coinmarker.presentation.util.strategy

import com.coinmarker.coinmarker.data.model.AssetDto
import com.coinmarker.coinmarker.presentation.util.type.SortingType

class LastAssetSortingStrategy(override var sortingType: SortingType = SortingType.DESCENDING) : AssetSortingStrategy {
    override fun sort(items: List<AssetDto>): List<AssetDto> {
        return if (sortingType == SortingType.ASCENDING) {
            items.sortedBy { it.last.replace(",","").toDouble()}
        } else {
            items.sortedByDescending { it.last.replace(",","").toDouble()}
        }
    }
    override fun strategyWithSortingType(): AssetSortingStrategy {
        return LastAssetSortingStrategy(sortingType)
    }
}