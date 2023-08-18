package com.coinmarker.coinmarker.presentation.util.strategy

import com.coinmarker.coinmarker.data.model.AssetDto
import com.coinmarker.coinmarker.presentation.util.type.SortingType

class AssetNameAssetSortingStrategy(override var sortingType: SortingType= SortingType.DESCENDING) : AssetSortingStrategy {
    override fun sort(items: List<AssetDto>): List<AssetDto> {
        return if (sortingType == SortingType.ASCENDING) {
            items.sortedBy { it.currencyPair }
        } else {
            items.sortedByDescending { it.currencyPair }
        }
    }

    override fun strategyWithSortingType(): AssetSortingStrategy {
        return AssetNameAssetSortingStrategy(sortingType)
    }
}