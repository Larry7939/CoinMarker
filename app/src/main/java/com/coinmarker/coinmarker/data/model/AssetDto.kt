package com.coinmarker.coinmarker.data.model

data class AssetDto(
    val currencyPair: String,
    val last: String,
    val changePercent: String,
    val changePrice: String,
    val volume: String,
    val timeStamp: Long
)
