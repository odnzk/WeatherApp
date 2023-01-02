package com.example.weatherapp.app.di

import com.example.data.Api
import com.example.data.repository.WeatherRepositoryImpl
import com.example.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/"
private const val API_KEY = "b65750fa8912403310944973c7362a56"
private const val QUERY_API_KEY = "appid"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun providesOkHttp(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    @Singleton
    fun providesRepository(api: Api): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }

    private val apiKeyInterceptor = Interceptor { chain ->
        val original = chain.request()
        val newURL = original.url().newBuilder()
            .addQueryParameter(QUERY_API_KEY, API_KEY)
            .build()
        chain.proceed(
            original.newBuilder()
                .url(newURL)
                .build()
        )
    }

}
