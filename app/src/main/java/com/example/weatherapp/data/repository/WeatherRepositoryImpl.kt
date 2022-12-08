package com.example.weatherapp.data.repository

import com.example.weatherapp.data.Api
import com.example.weatherapp.data.response.WeatherForecast
import javax.inject.Inject

private const val API_KEY = "b65750fa8912403310944973c7362a56"

class WeatherRepositoryImpl @Inject constructor(private val api: Api) : WeatherRepository {

    override suspend fun getWeatherForecast(latitude: Double, longitude: Double): WeatherForecast {
        return api.getWeatherForecast(latitude, longitude, API_KEY)
    }

    override suspend fun getWeatherForecast(cityName: String): WeatherForecast {
        return api.getWeatherForecast(cityName, API_KEY)
    }
}
