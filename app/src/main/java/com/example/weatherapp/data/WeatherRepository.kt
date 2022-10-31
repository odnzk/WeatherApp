package com.example.weatherapp.data

import com.example.weatherapp.data.response.WeatherForecast
import javax.inject.Inject

private const val API_KEY = "b65750fa8912403310944973c7362a56"

class WeatherRepository @Inject constructor(private val api: Api) {

    suspend fun getWeatherForecast(latitude: Double, longitude: Double): WeatherForecast {
        return api.getWeatherForecast(latitude, longitude, API_KEY)
    }

    suspend fun getWeatherForecast(cityName: String): WeatherForecast {
        return api.getWeatherForecast(cityName, API_KEY)
    }
}
