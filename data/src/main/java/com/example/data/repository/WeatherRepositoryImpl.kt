package com.example.data.repository


import com.example.data.Api
import com.example.domain.model.WeatherForecast
import com.example.domain.repository.WeatherRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: Api) :
    WeatherRepository {

    override suspend fun getWeatherForecast(
        latitude: Double,
        longitude: Double
    ): Result<WeatherForecast> =
        try {
            Result.success(api.getWeatherForecast(latitude, longitude, API_KEY))
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        }

    override suspend fun getWeatherForecast(cityName: String): Result<WeatherForecast> =
        try {
            Result.success(api.getWeatherForecast(cityName, API_KEY))
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        }

    companion object {
        private const val API_KEY = "b65750fa8912403310944973c7362a56"
    }
}
