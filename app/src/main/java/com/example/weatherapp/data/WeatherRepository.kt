package com.example.weatherapp.data

import com.example.weatherapp.data.response.WeatherForecast
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private const val API_KEY = "7772c8e5d929dc484f8137d8cf85e7fb"

class WeatherRepository {

    private val okhttp: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val api: Api by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .client(okhttp).addConverterFactory(GsonConverterFactory.create()).build()
            .create(Api::class.java)
    }


    suspend fun getWeatherForecast(latitude: Int, longitude: Int): WeatherForecast {
        return api.getWeatherForecast(latitude, longitude, API_KEY)
    }

    suspend fun getWeatherForecast(cityName: String): WeatherForecast {
        return api.getWeatherForecast(cityName, API_KEY)
    }
}
