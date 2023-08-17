package com.coinmarker.coinmarker.data.service

import retrofit2.http.GET

interface MarketService {
    @GET("/v1/ticker/detailed/all")
    suspend fun getAssetInfo(): String
}