package com.coinmarker.coinmarker.presentation.util.strategy

import com.coinmarker.coinmarker.data.model.AssetDto

class AssetSorter(private var assetSortingStrategy: AssetSortingStrategy) {
    fun setSortingStrategy(strategy:AssetSortingStrategy){
        assetSortingStrategy = strategy
    }
    fun sort(items:List<AssetDto>):List<AssetDto>{
        return assetSortingStrategy.sort(items)
    }
}