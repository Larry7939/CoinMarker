package com.coinmarker.coinmarker.presentation.util.strategy

import com.coinmarker.coinmarker.data.model.AssetDto
import com.coinmarker.coinmarker.presentation.util.type.SortingType
import timber.log.Timber

class VolumeAssetSortingStrategy(override var sortingType: SortingType = SortingType.DESCENDING) : AssetSortingStrategy {
    override fun sort(items: List<AssetDto>): List<AssetDto> {
        return if (sortingType == SortingType.ASCENDING) {
            items.sortedBy {it.volume.replace(",","").toDouble() }
        } else {
            items.sortedByDescending { it.volume.replace(",","").toDouble() }
        }
    }
    override fun strategyWithSortingType(): AssetSortingStrategy {
        return VolumeAssetSortingStrategy(sortingType)
    }
}