package com.coinmarker.coinmarker.domain

import com.coinmarker.coinmarker.data.model.AssetDto

interface MarketRepository {
    suspend fun getAssetInfo(): MutableList<AssetDto>
}