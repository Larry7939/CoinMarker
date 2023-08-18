package com.coinmarker.coinmarker.data.repository

import com.coinmarker.coinmarker.data.model.AssetDto
import com.coinmarker.coinmarker.data.model.response.ResponseGetAssetDetail
import com.coinmarker.coinmarker.data.source.MarketDataSource
import com.coinmarker.coinmarker.data.util.addCommasToNumber
import com.coinmarker.coinmarker.data.util.setPercentFormat
import com.coinmarker.coinmarker.data.util.setPriceFormat
import com.coinmarker.coinmarker.data.util.toVolumeInteger
import com.coinmarker.coinmarker.domain.MarketRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(private val marketDataSource: MarketDataSource) :
    MarketRepository {
    override suspend fun getMarketAssets(): MutableList<AssetDto> {
        val assets = mutableListOf<AssetDto>()
        val jsonStr = marketDataSource.getMarketAssets()
        val gson = Gson()
        val assetInfoMap: Map<String, ResponseGetAssetDetail> =
            gson.fromJson(
                jsonStr,
                object : TypeToken<Map<String, ResponseGetAssetDetail>>() {}.type
            )

        for (entry in assetInfoMap.entries) {
            with(entry) {
                assets.add(
                    AssetDto(
                        currencyPair = key.replace('_', '/').uppercase(Locale.ROOT),
                        last = value.last.addCommasToNumber().setPriceFormat(),
                        changePercent = value.changePercent.addCommasToNumber().setPercentFormat(),
                        changePrice = value.change.addCommasToNumber().setPriceFormat(),
                        volume = value.volume.toVolumeInteger().addCommasToNumber(),
                        timeStamp = value.timestamp
                    )
                )
            }
        }
        return assets
    }
}