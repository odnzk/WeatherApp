package com.example.domain.repository

import com.example.domain.model.WeatherForecast

interface WeatherRepository {

    suspend fun getWeatherForecast(latitude: Double, longitude: Double): Result<WeatherForecast>

    suspend fun getWeatherForecast(cityName: String): Result<WeatherForecast>
}
