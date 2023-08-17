package com.coinmarker.coinmarker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetEntity(
    val currencyPair: String,
    val last: String,
    val changePercent: String,
    val changePrice: String,
    val volume: String,
    val timeStamp: Long

) {
    fun toAssetDto(): AssetDto {
        return AssetDto(currencyPair, last, changePercent, changePrice, volume, timeStamp)
    }
}
