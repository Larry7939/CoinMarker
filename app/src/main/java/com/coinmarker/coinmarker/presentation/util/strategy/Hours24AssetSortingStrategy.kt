package com.coinmarker.coinmarker.presentation.util.strategy

import com.coinmarker.coinmarker.data.model.AssetDto
import com.coinmarker.coinmarker.presentation.util.type.SortingType

class Hours24AssetSortingStrategy(override var sortingType: SortingType = SortingType.DESCENDING) : AssetSortingStrategy {
    override fun sort(items: List<AssetDto>): List<AssetDto> {
        return if (sortingType == SortingType.ASCENDING) {
            items.sortedBy { it.timeStamp }
        } else {
            items.sortedByDescending { it.timeStamp }
        }
    }
    override fun strategyWithSortingType(): AssetSortingStrategy {
        return Hours24AssetSortingStrategy(sortingType)
    }
}