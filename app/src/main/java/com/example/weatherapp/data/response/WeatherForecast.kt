package com.example.weatherapp.data.response

data class WeatherForecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<MainInformationAboutDay>,
    val message: Int
)
