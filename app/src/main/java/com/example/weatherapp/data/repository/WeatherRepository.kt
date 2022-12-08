package com.example.weatherapp.data.repository

import com.example.weatherapp.data.response.WeatherForecast

interface WeatherRepository {

    suspend fun getWeatherForecast(latitude: Double, longitude: Double): WeatherForecast

    suspend fun getWeatherForecast(cityName: String): WeatherForecast
}
