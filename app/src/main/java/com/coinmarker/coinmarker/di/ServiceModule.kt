package com.coinmarker.coinmarker.di

import com.coinmarker.coinmarker.data.service.MarketService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Singleton
    @Provides
    fun provideMarketService(retrofit: Retrofit)=
        retrofit.create(MarketService::class.java)
}