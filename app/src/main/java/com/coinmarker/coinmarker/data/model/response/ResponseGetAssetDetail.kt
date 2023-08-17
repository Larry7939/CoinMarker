package com.coinmarker.coinmarker.data.model.response

data class ResponseGetAssetDetail (
        val ask: String,
        val bid: String,
        val change: String,
        val changePercent: String,
        val high: String,
        val last: String,
        val low: String,
        val `open`: String,
        val timestamp: Long,
        val volume: String
)