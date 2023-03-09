package com.example.weatherapp.app.di.modules

import android.app.Application
import com.example.data.BuildConfig
import com.example.data.api.OpenWeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

private const val WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/"
private const val API_KEY = BuildConfig.API_KEY
private const val QUERY_API_KEY = "appid"
private const val CACHE_DIRECTORY = "weather_cache"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @ApiKeyInterceptor
    fun providesApiKeyInterceptor(): Interceptor = Interceptor { chain ->
        val original = chain.request()
        val newURL = original.url.newBuilder().addQueryParameter(QUERY_API_KEY, API_KEY).build()
        chain.proceed(
            original.newBuilder().url(newURL).build()
        )
    }

    @Provides
    @CacheInterceptor
    fun providesCacheInterceptor(): Interceptor = Interceptor { chain ->
        chain.proceed(
            chain.request().newBuilder().cacheControl(
                CacheControl.Builder()
                    .maxAge(1, TimeUnit.MINUTES)
                    .build()
            ).build()
        )
    }

    @Singleton
    @Provides
    fun providesOkHtppClient(
        application: Application,
        @CacheInterceptor cacheInterceptor: Interceptor,
        @ApiKeyInterceptor apiKeyInterceptor: Interceptor,
        @LoggingInterceptor loggingInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().cache(
            Cache(
                directory = File(application.cacheDir, CACHE_DIRECTORY),
                maxSize = 50L * 1024L * 1024L
            )
        ).addInterceptor(cacheInterceptor).addInterceptor(loggingInterceptor)
            .addInterceptor(apiKeyInterceptor).build()
    }

    @Provides
    @LoggingInterceptor
    fun providesLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }

    @Provides
    @Singleton
    fun providesRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(client).build()


    @Provides
    @Singleton
    fun providesWeatherApi(retrofit: Retrofit): OpenWeatherApi =
        retrofit.create(OpenWeatherApi::class.java)
}


@Qualifier
annotation class LoggingInterceptor

@Qualifier
annotation class ApiKeyInterceptor

@Qualifier
annotation class CacheInterceptor
