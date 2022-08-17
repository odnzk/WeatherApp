package com.example.weatherapp

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

object WeatherRepository {

    private val okhttp: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val api: Api by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .client(okhttp).addConverterFactory(GsonConverterFactory.create()).build()
            .create(Api::class.java)
    }

    suspend fun getWeather(latitude: Double, longitude:Double){
        api.getWeather(latitude, longitude)
    }
}
