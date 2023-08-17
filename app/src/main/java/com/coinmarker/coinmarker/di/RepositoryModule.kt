package com.coinmarker.coinmarker.di

import com.coinmarker.coinmarker.data.repository.MarketRepositoryImpl
import com.coinmarker.coinmarker.domain.MarketRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Singleton
    @Binds
    fun bindMarketRepository(marketRepositoryImpl: MarketRepositoryImpl): MarketRepository
}