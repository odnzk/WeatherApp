package com.example.data

import com.example.domain.model.WeatherForecast
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

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
