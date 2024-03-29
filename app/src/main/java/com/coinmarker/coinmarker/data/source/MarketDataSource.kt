package com.coinmarker.coinmarker.data.source

import com.coinmarker.coinmarker.data.service.MarketService
import javax.inject.Inject


class MarketDataSource @Inject constructor(private val marketService: MarketService) {
    suspend fun getMarketAssets(): String {
        return marketService.getMarketAssets()
    }
}