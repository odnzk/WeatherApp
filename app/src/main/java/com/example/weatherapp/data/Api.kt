package com.example.weatherapp.data

import com.example.weatherapp.data.response.WeatherForecast
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    // api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("lat") latitude: Int,
        @Query("lon") longitude: Int,
        @Query("appid") apikey: String
    ): WeatherForecast

    // api.openweathermap.org/data/2.5/forecast?q={city name},{country code}&appid={API key}
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("q") cityName: String,
        @Query("q") countryCode: String,
        @Query("appid") apikey: String
    ): WeatherForecast

}
