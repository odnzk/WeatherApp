package com.example.data.repository


import com.example.data.Api
import javax.inject.Inject

private const val API_KEY = "b65750fa8912403310944973c7362a56"

class WeatherRepositoryImpl @Inject constructor(private val api: Api) :
    com.example.domain.repository.WeatherRepository {

    override suspend fun getWeatherForecast(
        latitude: Double,
        longitude: Double
    ): com.example.domain.model.WeatherForecast {
        return api.getWeatherForecast(latitude, longitude, API_KEY)
    }

    override suspend fun getWeatherForecast(cityName: String): com.example.domain.model.WeatherForecast {
        return api.getWeatherForecast(cityName, API_KEY)
    }
}
