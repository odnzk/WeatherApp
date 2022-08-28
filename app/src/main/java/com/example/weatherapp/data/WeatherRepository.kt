package com.example.weatherapp.data

import com.example.weatherapp.data.response.WeatherForecast

private const val API_KEY = "7772c8e5d929dc484f8137d8cf85e7fb"

class WeatherRepository(private val api: Api) {

    suspend fun getWeatherForecast(latitude: Int, longitude: Int): WeatherForecast {
        return api.getWeatherForecast(latitude, longitude, API_KEY)
    }

    suspend fun getWeatherForecast(cityName: String): WeatherForecast {
        return api.getWeatherForecast(cityName, API_KEY)
    }
}
