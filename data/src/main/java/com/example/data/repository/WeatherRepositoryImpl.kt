package com.example.data.repository


import com.example.data.Api
import com.example.domain.model.WeatherForecast
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: Api) :
    com.example.domain.repository.WeatherRepository {

    override suspend fun getWeatherForecast(
        latitude: Double,
        longitude: Double
    ): WeatherForecast {
        return api.getWeatherForecast(latitude, longitude, API_KEY)
    }

    override suspend fun getWeatherForecast(cityName: String): WeatherForecast {
        return api.getWeatherForecast(cityName, API_KEY)
    }

    companion object {
        private const val API_KEY = "b65750fa8912403310944973c7362a56"
    }
}
