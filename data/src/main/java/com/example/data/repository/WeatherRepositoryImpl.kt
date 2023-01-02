package com.example.data.repository


import com.example.data.Api
import com.example.domain.model.WeatherForecast
import com.example.domain.repository.WeatherRepository
import com.example.domain.state.State
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: Api) :
    WeatherRepository {

    override suspend fun getWeatherForecast(
        latitude: Double,
        longitude: Double
    ): State<WeatherForecast> =
        try {
            State.Success(api.getWeatherForecast(latitude, longitude))
        } catch (e: IOException) {
            State.Error(e)
        } catch (e: HttpException) {
            State.Error(e)
        }

    override suspend fun getWeatherForecast(cityName: String): State<WeatherForecast> =
        try {
            State.Success(api.getWeatherForecast(cityName))
        } catch (e: IOException) {
            State.Error(e)
        } catch (e: HttpException) {
            State.Error(e)
        }
}
