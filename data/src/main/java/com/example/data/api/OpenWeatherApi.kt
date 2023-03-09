package com.example.data.api

import com.example.data.BuildConfig
import com.example.domain.model.weather.WeatherForecast
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/"
private const val API_KEY = BuildConfig.API_KEY
private const val QUERY_API_KEY = "appid"

interface OpenWeatherApi {

    // api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): WeatherForecast

    // api.openweathermap.org/data/2.5/forecast?q={city name}&appid={API key}
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("q") cityName: String
    ): WeatherForecast

}

private val apiKeyInterceptor = Interceptor { chain ->
    val original = chain.request()
    val newURL = original.url.newBuilder()
        .addQueryParameter(QUERY_API_KEY, API_KEY)
        .build()
    chain.proceed(
        original.newBuilder()
            .url(newURL)
            .build()
    )
}

fun OpenWeatherApi(): OpenWeatherApi {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(apiKeyInterceptor)
        .build() //todo cache strategy
    val retrofit = Retrofit.Builder()
        .baseUrl(WEATHER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    return retrofit.create(OpenWeatherApi::class.java)
}
