package com.coinmarker.coinmarker.di

import com.coinmarker.coinmarker.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(
        logger: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder().addInterceptor(logger).build()

    @Provides
    @Singleton
    fun provideLogger(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @OptIn(InternalCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        kotlinx.coroutines.internal.synchronized(this) {
            val retrofit = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit ?: throw RuntimeException("Retrofit creation failed.")
        }
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        isLenient = true
        prettyPrint = true
        explicitNulls = false
        ignoreUnknownKeys = true
    }
}