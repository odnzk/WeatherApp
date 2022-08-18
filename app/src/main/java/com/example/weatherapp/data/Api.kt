package com.example.weatherapp.data

import com.example.weatherapp.data.response.WeatherForecast
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("lat") latitude: Int,
        @Query("lon") longitude: Int,
        @Query("appid") apikey: String
    ): WeatherForecast

}
