package com.example.data.repository


import com.example.data.api.OpenWeatherApi
import com.example.domain.model.state.State
import com.example.domain.model.weather.WeatherForecast
import com.example.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: OpenWeatherApi) : BaseNetworkRepository(),
    WeatherRepository {

    override suspend fun getWeatherForecast(
        latitude: Double, longitude: Double
    ): State<WeatherForecast> = safeRequest { api.getWeatherForecast(latitude, longitude) }

    override suspend fun getWeatherForecast(cityName: String): State<WeatherForecast> =
        safeRequest { api.getWeatherForecast(cityName) }
}
